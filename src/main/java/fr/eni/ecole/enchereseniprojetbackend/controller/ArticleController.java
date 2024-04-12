package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.ArticleFormInput;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.SearchFilterInput;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.CategorieService;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

    public ArticleController(ArticlesService as, CategorieService cs, UtilisateurService us) {
        this.as = as;
        this.cs = cs;
        this.us = us;
    }

    @GetMapping
    public List<Article> listarticle() {
        return as.consulterArticle();
    }

    @PostMapping
    public List<Article> listarticle(@RequestBody @Valid SearchFilterInput searchFilter) {
        if (searchFilter.countTrueBooleans() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too many filters!");
        }

        if (searchFilter.getUserId() != null && searchFilter.getUserId() != 0) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null ) {
                UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) auth.getPrincipal();
                if (userDetails.getUtilisateur().getId() == searchFilter.getUserId()) {
                    return as.getArticlesBySearchFilter(searchFilter);
                }
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }

        return as.getArticlesBySearchFilter(searchFilter);
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

    @PostMapping(path = "/add")
    public void addArticle(@RequestBody @Valid ArticleFormInput articleForm) {
        //, @RequestPart("file") MultipartFile file) throws IOException
//        byte[] bytes = file.getBytes();
//        Path path = Paths.get("./img/" + file.getOriginalFilename());
//        Files.write(path, bytes);
//        article.setImage(String.valueOf(path));
        as.creerArticle(toArticle(articleForm));
    }

    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        as.supprimerArticle(id);
        return "redirect:/";
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
