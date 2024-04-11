package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.SearchFilterInput;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.EtatVente;
import fr.eni.ecole.enchereseniprojetbackend.dal.ArticleRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.RetraitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class ArticlesServicesImpl implements ArticlesService {

    final ArticleRepository articleRepository;
    final RetraitRepository rr;

    public ArticlesServicesImpl(ArticleRepository articleRepository, RetraitRepository rr) {
        this.articleRepository = articleRepository;
        this.rr = rr;
    }

    @Override
    public List<Article> getArticlesBySearchFilter(SearchFilterInput searchFilter) {
        String userId = (searchFilter.getUserId() == null)
                ? null : searchFilter.getUserId().toString();
        String search = (searchFilter.getSearch() == null || searchFilter.getSearch().isBlank())
                ? null : searchFilter.getSearch();
        String categorieId = (searchFilter.getCategorieId() == null)
                ? null : searchFilter.getCategorieId().toString();

        return articleRepository.findArticlesByFilter(
                userId, search, categorieId,
                LocalDateTime.now(), searchFilter.isOpenBids(), searchFilter.isOngoingBids(), searchFilter.isWonBids(),
                searchFilter.isOngoingSales(), searchFilter.isNotStartedSales(), searchFilter.isCompletedSales());
    }

    @Override
    public List<Article> consulterArticle() {
        List<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
            EtatVente etatVente = article.getEtatVente();
        }
        return articles;
    }

    @Override
    public Article consulterArticleParId(long id) {
        return articleRepository.findByid(id);
    }

    @Override
    public void creerArticle(Article article) {
        rr.save(article.getRetrait());
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
