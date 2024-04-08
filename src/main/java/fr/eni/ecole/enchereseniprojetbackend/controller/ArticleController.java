package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
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

    public ArticleController(ArticlesService as) {
        this.as = as;
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

    @PostMapping("/add")
    public String addArticle(Article article) {
        as.creerArticle(article);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        as.supprimerArticle(id);
        return "redirect:/";
    }
}
