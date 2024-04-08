package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UtilisateurController {

    @Autowired
    UtilisateurService us;

    @GetMapping
    public List<Utilisateur> getUsers() {
        return us.getUsers();
    }

    @PostMapping
    public void addUser(@RequestBody @Valid Utilisateur user) {
        us.addUser(user);
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

