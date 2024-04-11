package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.ArticleFormInput;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.EnchereFormInput;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.CategorieService;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
