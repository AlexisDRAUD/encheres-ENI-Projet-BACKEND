package fr.eni.ecole.enchereseniprojetbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import fr.eni.ecole.enchereseniprojetbackend.bll.Utilisateurs;

@Controller
@RequestMapping("/users")
public class UtilisateurController {

    private final Utilisateurs utilisateures;

    public UtilisateurController(Utilisateurs utilisateures) {
        this.utilisateures = utilisateures;
    }
}

