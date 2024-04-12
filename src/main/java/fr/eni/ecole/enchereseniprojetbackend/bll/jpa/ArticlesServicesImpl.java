package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.SearchFilterInput;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.EtatVente;
import fr.eni.ecole.enchereseniprojetbackend.dal.ArticleRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.RetraitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Page<Article> getArticlesBySearchFilter(SearchFilterInput searchFilter, Pageable pageable) {
        String userId = (searchFilter.getUserId() == 0)
                ? null : searchFilter.getUserId().toString();
        String search = (searchFilter.getSearch() == null || searchFilter.getSearch().isBlank())
                ? null : searchFilter.getSearch();
        String categorieId = (searchFilter.getCategorieId() == 0)
                ? null : searchFilter.getCategorieId().toString();

        return articleRepository.findArticlesByFilter(
                userId, search, categorieId,
                LocalDateTime.now(), searchFilter.isOpenBids(), searchFilter.isOngoingBids(), searchFilter.isWonBids(),
                searchFilter.isOngoingSales(), searchFilter.isNotStartedSales(), searchFilter.isCompletedSales(), pageable);
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
