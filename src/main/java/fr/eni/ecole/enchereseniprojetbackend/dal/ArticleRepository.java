package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findByid(long id);

    @Query(value = "select distinct a.* from article a " +
            "left join enchere e on a.id = e.article_id " +
            "where (:nomArticle is null or LOWER(a.nom_article) LIKE %:nomArticle%)" +
            "AND (:categorieId is null or a.categorie_id=:categorieId)" +
            "AND ((:openBids is true AND (a.date_debut < :currentTime AND a.date_fin > :currentTime))" +
            "OR (:ongoingBids is true AND (a.date_debut < :currentTime AND a.date_fin > :currentTime AND e.utilisateur_id = :userId))" +
            "OR (:wonBids is true AND (a.date_fin < :currentTime AND a.acheteur_id = :userId))" +
            "OR (:ongoingSales is true AND (a.vendeur_id = :userId AND a.date_debut < :currentTime AND a.date_fin > :currentTime))" +
            "OR (:notStartedSales is true AND (a.vendeur_id = :userId AND a.date_debut > :currentTime))" +
            "OR (:completedSales is true AND (a.vendeur_id = :userId AND a.date_fin < :currentTime)))" +
            "ORDER BY a.date_debut DESC",

            countQuery = "SELECT COUNT(DISTINCT a.id) from article a " +
                    "left join enchere e on a.id = e.article_id " +
                    "where (:nomArticle is null or LOWER(a.nom_article) LIKE %:nomArticle%)" +
                    "AND (:categorieId is null or a.categorie_id=:categorieId)" +
                    "AND ((:openBids is true AND (a.date_debut < :currentTime AND a.date_fin > :currentTime))" +
                    "OR (:ongoingBids is true AND (a.date_debut < :currentTime AND a.date_fin > :currentTime AND e.utilisateur_id = :userId))" +
                    "OR (:wonBids is true AND (a.date_fin < :currentTime AND a.acheteur_id = :userId))" +
                    "OR (:ongoingSales is true AND (a.vendeur_id = :userId AND a.date_debut < :currentTime AND a.date_fin > :currentTime))" +
                    "OR (:notStartedSales is true AND (a.vendeur_id = :userId AND a.date_debut > :currentTime))" +
                    "OR (:completedSales is true AND (a.vendeur_id = :userId AND a.date_fin < :currentTime)))",
            nativeQuery = true)
    Page<Article> findArticlesByFilter(@Param("userId") String userId,
                                       @Param("nomArticle") String nomArticle,
                                       @Param("categorieId") String categorieId,
                                       @Param("currentTime") LocalDateTime currentTime,
                                       @Param("openBids") boolean openBids,
                                       @Param("ongoingBids") boolean ongoingBids,
                                       @Param("wonBids") boolean wonBids,
                                       @Param("ongoingSales") boolean ongoingSales,
                                       @Param("notStartedSales") boolean notStartedSales,
                                       @Param("completedSales") boolean completedSales,
                                       Pageable pageable);
}
