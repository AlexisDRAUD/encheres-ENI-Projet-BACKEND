package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.*;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.CategorieService;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bll.mock.RetraitService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import jakarta.validation.Valid;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public ArticleController(ArticlesService as, CategorieService cs, UtilisateurService us, RetraitService rs) {
        this.as = as;
        this.cs = cs;
        this.us = us;
        this.rs = rs;
    }

    @GetMapping
    public List<Article> listarticle() {
        return as.consulterArticle();
    }

    @PostMapping("/{pageNum}")
    public Page<Article> listarticle(@RequestBody @Valid SearchFilterInput searchFilter, @PathVariable("pageNum") int pageNum) {
        Pageable pageable = PageRequest.of(pageNum-1, 6);

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
    public ResponseEntity<Article> getArticleById(@PathVariable("id") int id) {
        Article article = as.consulterArticleParId(id);
        if (article != null) {
            return ResponseEntity.ok(article);
        }else {
            return ResponseEntity.accepted().build();
        }
    }

    @GetMapping("/modif/{id}")
    public ResponseEntity<ArticleForUpdate> getArticleForUpdateById(@PathVariable("id") int id) {
        System.out.println("id : " + id);
        Article article = as.consulterArticleParId(id);
        System.out.println("Article : " + article);
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
                    article.getRetrait().getVille()
            );
            System.out.println("articleForUpdate : " + articleForUpdate.toString());
            return ResponseEntity.ok(articleForUpdate);
        }else {
            return ResponseEntity.accepted().build();
        }
    }

    @PostMapping(path = "/add")
    public void addArticle(@RequestBody @Valid ArticleFormInput articleForm) {
        //, @RequestPart("file") MultipartFile file) throws IOException
//        byte[] bytes = file.getBytes();
//        Path path = Paths.get("./img/" + file.getOriginalFilename());
//        Files.write(path, bytes);
//        article.setImage(String.valueOf(path));

        as.creerArticle(toArticle(articleForm));
    }

    @PutMapping("/{id}")
    public void putArticle(@RequestBody @Valid ArticleForUpdate articleForUpdate,
                                     @PathVariable("id") Long id) {
        LocalDateTime date = LocalDateTime.now();
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

    @PostMapping("/delete/{id}")
    public void deleteArticle(@PathVariable Long id) {
        LocalDateTime date = LocalDateTime.now();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Article article = as.consulterArticleParId(id);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet article n'existe pas !");
        } else {
            if (auth != null ) {
                UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) auth.getPrincipal();
                System.out.println("idAuthenti: " + userDetails.getUtilisateur().getId());
                if(article.getDateDebut().isAfter(date) &&
                        userDetails.getUtilisateur().getId() == article.getVendeur().getId())
                {
                    long idRetrait = (rs.recupererRetraitById(id)).getId();
                    as.supprimerArticle(id);
                    Article articleVerif = as.consulterArticleParId(id);
                    if(articleVerif == null){
                        rs.deleteRetrait(idRetrait);
                    } else {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "L'article n'a pas été supprimé !");
                    }

                }else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous ne pouvez pas supprimer cet article !");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
            }
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
                new Retrait(articleForm.getRue(),articleForm.getCodePostal(), articleForm.getVille())
        );
    }
}
