package fr.eni.ecole.enchereseniprojetbackend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration // Nécessaire lorsqu'on définit des @Bean dans une classe
@EnableWebSecurity
public class SecurityConfiguration {
    /**
     * On définit un filtre de type SecurityFilterChain
     * qui va être ajouté au contexte Spring (car on a mis @Bean)
     *
     * Spring Security va comprendre que c'est ce filtre qu'il va devoir utiliser
     * à la place de son filtre par défaut (qui n'autorise aucune requête non authentifie)
     *
     * dans .requestMatchers, on peut mettre :
     * - soit une url : .requestMatchers("/pageConnecte")
     * - soit une liste d'url : .requestMatchers("/pageConnecte1", "/pageConnect2", "/pageConnecte3")
     * - soit une url avec un wildcard (*) : .requestMatchers("/prive/*")
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // on autorise la requête http entrante en fonction de ces critères :
        http.authorizeHttpRequests((authorize) -> authorize
                        // si jamais la requête veut aller sur l'url "/pageConnecte" : alors on doit être authentifié (.authenticated())
                        //.requestMatchers(regexMatcher("/films/.*/avis")).authenticated()
                        //.requestMatchers("/membres").authenticated()
                        // si jamais la requête veut aller sur l'url "/pageAdmin" : alors on doit avoir le rôle admin (.hasRole("admin"))
                        //.requestMatchers("/films/creer", "/participants").hasRole("admin")
                        // pour toute les autres requête : on autorise sans contraintes
                        .requestMatchers("/**").permitAll()
                )
                // on utilise une authentification basique (utilisateur / mot de passe)
                .httpBasic(Customizer.withDefaults())
                // on utilise le formulaire par défaut proposé par Spring Security
                .formLogin(Customizer.withDefaults())
                // quand on se déconnecte=> on redirige vers l'accueil
                .logout((logout) -> logout.logoutSuccessUrl("/"));
        return http.build();
    }

    /**
     * On définit un bean pour l'utilitaire d'encryption de mot de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder : classe utilitaire de Spring Security qui decrypte/encrypte les mots de passe
        return new BCryptPasswordEncoder();
    }

/*
 * On définit un bean pour la gestion des utilisateurs en mémoire (solution temporaire)
 *
 * A partir du moment ou Spring voit qu'il y a un @Bean de type InMemoryUserDetailsManager dans le contexte Spring
 * Il va l'utiliser pour chercher les utilisateurs
*/
 @Bean
 public InMemoryUserDetailsManager userDetailsService() {
 // On a une liste d'utilisateurs Spring Security (interface UserDetails)
 List<UserDetails> userDetailsList = new ArrayList<>();
 // on définit un utilisateur avec nom "membre", mot de passe "membre123", role : "user"
 userDetailsList.add(
 User.withUsername("membre").password(passwordEncoder().encode("membre123"))
 .roles("user").build());
 // on définit un utilisateur avec nom "admin", mot de passe "admin123", role : "admin", "user"
 userDetailsList.add( User.withUsername("admin").password(passwordEncoder().encode("admin123"))
 .roles("admin", "user").build());

 return new InMemoryUserDetailsManager(userDetailsList);

 }


}
