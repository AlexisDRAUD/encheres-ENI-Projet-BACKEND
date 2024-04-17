package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.MoneyRequest;
import fr.eni.ecole.enchereseniprojetbackend.DTO.response.UserPayload;
import fr.eni.ecole.enchereseniprojetbackend.Security.JwtUtils;
import fr.eni.ecole.enchereseniprojetbackend.Security.MyUserDetailsService;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurDesactiveService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.bo.UtilisateurDesactive;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UtilisateurController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private EncheresService encheresService;

    @Autowired
    UtilisateurService us;

    @Autowired
    UtilisateurDesactiveService uds;


    @GetMapping()
    public ResponseEntity<List<Utilisateur>> getUsers() {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().isAdministrateur())
        {
            List<Utilisateur> utilisateurs = us.getUsers();
            if(utilisateurs != null){
                return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/disabled")
    public ResponseEntity<List<UtilisateurDesactive>> getUsersDisabled() {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().isAdministrateur())
        {
            List<UtilisateurDesactive> utilisateursDisabled = uds.getUsers();
            if(utilisateursDisabled != null){
                return new ResponseEntity<>(utilisateursDisabled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public UserPayload getUserById(@PathVariable("id") Long id) {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().getId() == id)
        {
            return us.getUserById(id).toUserPayload();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@RequestBody @Valid UserFormInput userForm, BindingResult br,
                                     @PathVariable("id") Long id) {
        Map<String, String> errors = new HashMap<>();

        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().getId() == id)
        {
            if (us.getUserById(id) != null) {
                if (br.hasErrors()) {
                    for (FieldError error : br.getFieldErrors()) {
                        errors.put(error.getField(), error.getDefaultMessage());
                    }
                    return ResponseEntity.badRequest().body(errors);
                }

                errors = us.updateUser(userForm, id, errors);

                if (!errors.isEmpty()) {
                    return ResponseEntity
                            .badRequest()
                            .body(errors);
                }

            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }

        return ResponseEntity.ok().body("L'utilisateur a été modifié.");
    }

    @DeleteMapping("/{id}/{isForDisabled}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id, @PathVariable("isForDisabled") Boolean isForDisabled) {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUtilisateur().getId() == id || userDetails.getUtilisateur().isAdministrateur())
        {
            try {
                us.deleteUserById(id, isForDisabled);
            } catch (ResponseStatusException error){
                throw error;
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vous n'avez pas accès à cet utilisateur !");
        }
    }

    @DeleteMapping("/reactiver/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        UtilisateurDesactive utilisateurDesactive = uds.getUserDesactiveById(id);
        if(utilisateurDesactive != null){
            Utilisateur u = new Utilisateur(
                    utilisateurDesactive.getUsername(),
                    utilisateurDesactive.getPrenom(),
                    utilisateurDesactive.getNom(),
                    utilisateurDesactive.getEmail(),
                    utilisateurDesactive.getTelephone(),
                    utilisateurDesactive.getRue(),
                    utilisateurDesactive.getCodePostal(),
                    utilisateurDesactive.getVille(),
                    utilisateurDesactive.getPassword(),
                    utilisateurDesactive.getCredit(),
                    utilisateurDesactive.isAdministrateur()
            );
            uds.userReactive(u);
            uds.deleteUserDesactiveById(id);
            throw new ResponseStatusException(HttpStatus.OK, "utilisateur réactivé avec succès");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'a pas été désactivé !");
        }
    }


    @PostMapping("/credit")
    public ResponseEntity<String> addMoney(@RequestBody MoneyRequest moneyRequest) {
        try {
            Map<String, String> errors = new HashMap<>();
            errors = us.addMoney(moneyRequest.getId(), moneyRequest.getMoney(), errors);

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors.toString());
            }
            return ResponseEntity.ok("Ajout de points avec succès");
        } catch (Exception error) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout de points: " + error.getMessage());
        }
    }


}

