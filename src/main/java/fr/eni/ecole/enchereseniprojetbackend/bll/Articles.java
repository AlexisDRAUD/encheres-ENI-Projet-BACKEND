package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Article;

import java.util.List;

public interface Articles {

    List<Article> consulterArticle();
    Article consulterArticleParId(long id);

    void creerArticle(Article article);


    void editArticle(Article article);

    void supprimerArticle(long id);

}
