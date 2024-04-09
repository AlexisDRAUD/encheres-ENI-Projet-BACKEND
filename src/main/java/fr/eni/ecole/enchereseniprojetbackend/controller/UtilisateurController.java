package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.Security.JwtUtils;
import fr.eni.ecole.enchereseniprojetbackend.Security.MyUserDetailsService;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UtilisateurController {

    @Autowired
    UtilisateurService us;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @GetMapping
    public Utilisateur getConnectedUser(HttpServletRequest request) throws Exception {
        String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.parseJwt(request));
        UtilisateurSpringSecurity user = (UtilisateurSpringSecurity) userDetailsService.loadUserByUsername(username);
        return user.getUtilisateur();
    }




/*
    @GetMapping
    public List<Utilisateur> getUsers() {
        return us.getUsers();
    }*/

    @GetMapping("/{id}")
    public Utilisateur getUserById(@PathVariable("id") Long id) {
        return us.getUserById(id);
    }

    @PutMapping("/{id}")
    public void putUser(@RequestBody @Valid Utilisateur user,
                           @PathVariable("id") Long id) {
        if (us.getUserById(id) != null) {
            user.setId(id);
            us.updateUser(user);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        if (us.getUserById(id) != null) {
            us.deleteUserById(id);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
        }
    }
}

