package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.SearchFilterInput;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ArticlesService {
    Page<Article> getArticlesBySearchFilter(SearchFilterInput searchFilter, Pageable pageable);

    List<Article> consulterArticle();

    Article consulterArticleParId(long id);

    void creerArticle(Article article);

    void editArticle(Article article);

    void supprimerArticle(long id);

}
