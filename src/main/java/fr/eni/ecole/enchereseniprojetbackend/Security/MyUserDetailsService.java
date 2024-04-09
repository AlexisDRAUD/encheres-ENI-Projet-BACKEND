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
     * => à partir du service utilisateurService
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurService.recupererUtilisateurParPseudo(username);

        // Si l'utilisateur n'est pas trouvé : je lance une exception afin que Spring Security affiche une erreur dans le formulaire
        if (utilisateur == null){
            throw new UsernameNotFoundException(username);
        }

        // Si on a trouvé un utilisateur, je retourne un objet UserDetails Spring Security qui englobe les informations de l'utilisateur
        return new UtilisateurSpringSecurity(utilisateur);
    }
}