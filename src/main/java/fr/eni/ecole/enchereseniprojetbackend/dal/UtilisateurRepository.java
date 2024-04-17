package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Utilisateur findById(long id);

    Utilisateur findByUsername(String username);

    Utilisateur findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
