package fr.eni.ecole.enchereseniprojetbackend.Security;

import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurService utilisateurService;

    /**
     * Comment est-ce qu'on va chercher un utilisateur Spring Security à partir d'un pseudo?
     * => à partir du service membreService
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurService.recupererUtilisateurParPseudo(username);

        // si le membre n'est pas trouvé : je lance une exception afin que Spring Security affiche une erreur dans le formulaire
        if (utilisateur == null){
            throw new UsernameNotFoundException(username);
        }

        // si on a trouvé un membre, je retourne un utilisateur Spring Security qui englobe le membre
        return new UtilisateurSpringSecurity(utilisateur);
    }
}
