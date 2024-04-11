package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.response.UserPayload;
import fr.eni.ecole.enchereseniprojetbackend.Security.JwtUtils;
import fr.eni.ecole.enchereseniprojetbackend.Security.MyUserDetailsService;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
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

import java.util.HashMap;
import java.util.Map;

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

                if (us.usernameAlreadyExist(userForm.getUsername()) && !userForm.getUsername().equals(userDetails.getUsername())) {
                    errors.put("username", "Username is already taken!");
                }

                if (us.emailAlreadyExist(userForm.getEmail()) && !userForm.getEmail().equals(userDetails.getEmail())) {
                    errors.put("email", "Email is already in use!");
                }

                if (userForm.getPassword() != null && !userForm.getPassword().isBlank()) {
                    if (userForm.getOldPassword() == null || userForm.getOldPassword().isBlank() || !us.isValidOldPassword(userForm.getOldPassword(),
                            us.getUserById(id).getPassword())) {
                        errors.put("oldPassword", "Actual password is incorrect!");
                    }

                    if (userForm.getPassword().length() >= 6 && userForm.getPassword().length() <= 30) {
                        if (!us.isValidPassword(userForm.getPassword(), userForm.getPasswordConfirmation())) {
                            errors.put("password", "Passwords do not match!");
                        }
                    } else {
                        errors.put("password", "Le taille du mot de passe doit être compris entre 6 et 30!");
                    }
                }

                if (!errors.isEmpty()) {
                    return ResponseEntity
                            .badRequest()
                            .body(errors);
                }

                us.updateUser(userForm, id);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }

        return ResponseEntity.ok().body("L'utilisateur a été modifié.");
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().getId() == id)
        {
            if (us.getUserById(id) != null) {
                us.deleteUserById(id);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }
    }
}

