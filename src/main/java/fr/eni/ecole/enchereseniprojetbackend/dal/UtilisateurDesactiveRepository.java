package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.UtilisateurDesactive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurDesactiveRepository extends JpaRepository<UtilisateurDesactive, Long> {
}
