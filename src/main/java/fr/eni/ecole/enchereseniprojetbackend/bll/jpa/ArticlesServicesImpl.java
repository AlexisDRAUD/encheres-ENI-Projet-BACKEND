package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.dal.ArticleRepository;

import java.util.Collections;
import java.util.List;

public class ArticlesServicesImpl implements ArticlesService {

    final ArticleRepository articleRepository;

    public ArticlesServicesImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> consulterArticle() {
        return articleRepository.findAll();
    }

    @Override
    public Article consulterArticleParId(long id) {
        return articleRepository.findByid(id);
    }

    @Override
    public void creerArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void editArticle(Article article) {
        Article existingArticle = articleRepository.findById(article.getId()).orElse(null);

        if (existingArticle != null) {
            existingArticle.setNomArticle(article.getNomArticle());
            existingArticle.setCategorie(article.getCategorie());
            existingArticle.setDescription(article.getDescription());
            existingArticle.setRetrait(article.getRetrait());

            articleRepository.save(existingArticle);
        }

    }

    @Override
    public void supprimerArticle(long id) {
        articleRepository.deleteAllById(Collections.singleton(id));
    }
}
