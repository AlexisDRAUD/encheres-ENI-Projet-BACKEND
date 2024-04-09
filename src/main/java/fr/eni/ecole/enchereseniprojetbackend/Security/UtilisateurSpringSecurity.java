package fr.eni.ecole.enchereseniprojetbackend.Security;

import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurSpringSecurity implements UserDetails {
    /*
     * un utilisateur Spring Security va possèder un membre (qui sera donc accessible lorsqu'on recuperera l'utilisateur connexcté)
     */
    private Utilisateur utilisateur;

    /**
     * Spring Security doit pouvoir savoir comment il recupère la liste des Permissions (GrantedAuthority)
     * => à partir du membre
     *
     * note : dans Spring security, on a Permission = "ROLE_xxx" correspond au rôle xxx
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // si jamais le mebre possède l'attribut admin à true
        // => il a le rôle admin (= la permission "ROLE_admin") + le rôle user (= la permission "ROLE_user")
        if (utilisateur.isAdministrateur()) {
            return List.of(new SimpleGrantedAuthority("ROLE_admin"), new SimpleGrantedAuthority("ROLE_user"));
        }
        // sinon il a le role "user"
        return List.of(new SimpleGrantedAuthority("ROLE_user"));
    }

    /**
     * Spring Security doit pouvoir savoir comment il recupère le mot de passe
     * => à partir du membre
     */
    @Override
    public String getPassword() {
        return utilisateur.getPassword();
    }

    /**
     * Spring Security doit pouvoir savoir comment il recupère le nom d'utilisateur
     * => à partir du membre
     */
    @Override
    public String getUsername() {
        return utilisateur.getPseudo();
    }

    public String getEmail() {
        return utilisateur.getEmail();
    }

    /**
     * Spring Security doit pouvoir savoir si notre utilisateur a son compte non expiré
     * => on va metre "true" tout le temps
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Spring Security doit pouvoir savoir si notre utilisateur est non bloqué o
     * => on va metre "true" tout le temps
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Spring Security doit pouvoir savoir si notre utilisateur a ses identifiés non expirés
     * => on va metre "true" tout le temps
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Spring Security doit pouvoir savoir si notre utilisateur est activé ou non
     * => on va metre "true" tout le temps
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

