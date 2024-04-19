package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.*;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.CategorieService;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bll.RetraitService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;
import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private final ArticlesService as;

    @Autowired
    private final CategorieService cs;

    @Autowired
    private final UtilisateurService us;

    @Autowired
    private final RetraitService rs;

    @Autowired
    private final UtilisateurRepository ur;

    public ArticleController(ArticlesService as, CategorieService cs, UtilisateurService us, RetraitService rs, UtilisateurRepository ur) {
        this.as = as;
        this.cs = cs;
        this.us = us;
        this.rs = rs;
        this.ur = ur;
    }

    @GetMapping
    public List<Article> listarticle() {
        return as.consulterArticle();
    }

    @PostMapping("/{pageNum}")
    public Page<Article> listarticle(@RequestBody @Valid SearchFilterInput searchFilter, @PathVariable("pageNum") int pageNum) {
        Pageable pageable = PageRequest.of(pageNum-1, 6, Sort.by("date_debut").descending());

        if (searchFilter.countTrueBooleans() > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too many filters!");
        }

        if (searchFilter.getUserId() != null && searchFilter.getUserId() != 0) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null ) {
                UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) auth.getPrincipal();
                if (userDetails.getUtilisateur().getId() == searchFilter.getUserId()) {
                    return as.getArticlesBySearchFilter(searchFilter, pageable);
                }
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }

        return as.getArticlesBySearchFilter(searchFilter, pageable);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable("id") int id) {
        Article article = as.consulterArticleParId(id);
        if (article != null) {
            return ResponseEntity.ok(article);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/modif/{id}")
    public ResponseEntity<?> getArticleForUpdateById(@PathVariable("id") Long id) {
        Article article = as.consulterArticleParId(id);
        if (article != null) {
            ArticleForUpdate articleForUpdate = new ArticleForUpdate(
                    article.getId(),
                    article.getNomArticle(),
                    article.getDescription(),
                    article.getDateDebut(),
                    article.getDateFin(),
                    article.getMiseAPrix(),
                    article.getPrixVente(),
                    article.getCategorie().getId(),
                    article.getVendeur().getId(),
                    article.getRetrait().getRue(),
                    article.getRetrait().getCodePostal(),
                    article.getRetrait().getVille(),
                    article.getImg()
            );
            return ResponseEntity.ok(articleForUpdate);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addArticle(@RequestBody @Valid ArticleFormInput articleForm) {
        try {
            as.creerArticle(toArticle(articleForm));
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (ResponseStatusException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage());
        }
    }

    @PutMapping("/{id}/retire")
    public ResponseEntity<?> putArticleRetire(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) auth.getPrincipal();
        Article article = as.consulterArticleParId(id);

        if (auth != null &&
                (userDetails.getUtilisateur().getId() == article.getVendeur().getId() ||
                userDetails.getUtilisateur().getId() == article.getAcheteur().getId())
        ) {
            if(userDetails.getUtilisateur().getId() == article.getVendeur().getId()){
                article.setVendeurRetire(true);
                as.creerArticle(article);
            } else {
                article.setAcheteurRetire(true);
                as.creerArticle(article);
            }
            if(article.getVendeurRetire() == article.getAcheteurRetire() == true){
                article.getVendeur().setCredit(article.getVendeur().getCredit() + article.getPrixVente());

                ur.save(article.getVendeur());
            }
            throw new ResponseStatusException(HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès à cette article!");
        }
}

    @PutMapping("/{id}")
    public void putArticle(@RequestBody @Valid ArticleForUpdate articleForUpdate,
                                     @PathVariable("id") Long id) {
        LocalDateTime date = LocalDateTime.now().plusHours(2);
        System.out.println("date: " + date);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null ) {
            UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) auth.getPrincipal();
            System.out.println("idAuthenti: " + userDetails.getUtilisateur().getId());
            if(articleForUpdate.getDateDebut().isAfter(date) &&
                    userDetails.getUtilisateur().getId() == articleForUpdate.getVendeurId())
            {
                Article article = as.consulterArticleParId(id);
                if (article != null) {
                    article.setNomArticle(articleForUpdate.getNomArticle());
                    article.setDescription(articleForUpdate.getDescription());
                    article.setDateDebut(articleForUpdate.getDateDebut());
                    article.setDateFin(articleForUpdate.getDateFin());
                    article.setMiseAPrix(articleForUpdate.getMiseAPrix());
                    article.setPrixVente(articleForUpdate.getPrixVente());
                    article.setImg(articleForUpdate.getImg());
                    article.setVendeur(us.getUserById(articleForUpdate.getVendeurId()));
                    article.setCategorie(cs.consulterCategorieParId(articleForUpdate.getCategorieId()));

                    article.getRetrait().setRue(articleForUpdate.getRue());
                    article.getRetrait().setCodePostal(articleForUpdate.getCodePostal());
                    article.getRetrait().setVille(articleForUpdate.getVille());

                    System.out.println("Article avant modif: " + article);
                    as.editArticle(article);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet article n'existe pas !");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous ne pouvez pas modifier cet article !");
            }
                //return ResponseEntity.ok().body("L'article a été modifié.");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        LocalDateTime date = LocalDateTime.now().plusHours(2);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Article article = as.consulterArticleParId(id);

        if (auth != null ) {
            UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) auth.getPrincipal();
            if(article.getDateDebut().isAfter(date) &&
                    userDetails.getUtilisateur().getId() == article.getVendeur().getId())
            {
                if (article == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet article n'existe pas !");
                } else {
                    long idRetrait = (rs.recupererRetraitById(id)).getId();
                    as.supprimerArticle(id);
                    Article articleVerif = as.consulterArticleParId(id);
                    if(articleVerif == null){
                        rs.deleteRetrait(idRetrait);
                        throw new ResponseStatusException(HttpStatus.OK);
                    } else {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "L'article n'a pas été supprimé !");
                    }
                }
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous ne pouvez pas supprimer cet article !");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous devez être connecté !");
        }
    }

    public Article toArticle(ArticleFormInput articleForm) {
        return new Article(
                articleForm.getNomArticle(),
                articleForm.getDescription(),
                articleForm.getDateDebut(),
                articleForm.getDateFin(),
                articleForm.getMiseAPrix(),
                articleForm.getPrixVente(),
                cs.consulterCategorieParId(articleForm.getCategorieId()),
                us.getUserById(articleForm.getVendeurId()),
                articleForm.getImg(),
                new Retrait(articleForm.getRue(),articleForm.getCodePostal(), articleForm.getVille())
        );
    }
}
