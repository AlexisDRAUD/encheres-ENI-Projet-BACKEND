package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnchereRepository extends JpaRepository<Enchere, Long> {

    List<Enchere> findAllByUtilisateur_id (int id);

    List<Enchere> findAllByArticle_id (int id);


}
