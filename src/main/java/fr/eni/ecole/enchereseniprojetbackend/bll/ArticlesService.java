package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ArticlesService {

    List<Article> consulterArticle();
    Article consulterArticleParId(long id);

    void creerArticle(Article article);


    void editArticle(Article article);

    void supprimerArticle(long id);

}
