package fr.eni.ecole.enchereseniprojetbackend.dal;

import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Utilisateur findById(long id);

    Utilisateur findByPseudo(String pseudo);

    Utilisateur findByEmail(String email);

    Boolean existsByPseudo(String pseudo);

    Boolean existsByEmail(String email);
}
