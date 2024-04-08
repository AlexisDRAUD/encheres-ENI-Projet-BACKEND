package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ModelAttribute("articles")
    public List<Article> initializePersonne() {
        return as.consulterArticle();
    }

    @GetMapping
    public String listActeurs(Model model) {
        model.addAttribute("artciles", as.consulterArticle());
        return "articles";
    }

    @PostMapping("/add")
    public String addActeur(Article article) {
        as.creerArticle(article);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteActeur(@PathVariable Long id) {
        as.supprimerArticle(id);
        return "redirect:/";
    }
}
