package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;
import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    Categorie findByid(long id);


}
