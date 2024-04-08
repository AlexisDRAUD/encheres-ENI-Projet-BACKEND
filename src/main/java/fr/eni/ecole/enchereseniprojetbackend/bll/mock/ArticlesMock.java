package fr.eni.ecole.enchereseniprojetbackend.bll.mock;

import fr.eni.ecole.enchereseniprojetbackend.bll.Articles;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;

import java.util.List;

public class ArticlesMock implements Articles {
    @Override
    public List<Article> consulterArticle() {
        return null;
    }

    @Override
    public Article consulterArticleParId(long id) {
        return null;
    }

    @Override
    public void creerArticle(Article article) {

    }

    @Override
    public void editArticle(Article article) {

    }

    @Override
    public void supprimerArticle(long id) {

    }
}
