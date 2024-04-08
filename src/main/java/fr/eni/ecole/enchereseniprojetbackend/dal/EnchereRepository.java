package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnchereRepository extends JpaRepository<Enchere, Long> {
}
