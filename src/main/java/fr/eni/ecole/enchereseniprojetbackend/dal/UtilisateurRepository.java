package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Utilisateur findById(long id);

    Utilisateur findByUsername(String username);

    Utilisateur findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
