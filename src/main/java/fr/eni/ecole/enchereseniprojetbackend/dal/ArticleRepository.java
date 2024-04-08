package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findByid(long id);
}
