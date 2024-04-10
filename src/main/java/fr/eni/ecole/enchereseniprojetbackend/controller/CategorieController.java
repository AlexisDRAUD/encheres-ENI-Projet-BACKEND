package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.bll.CategorieService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/categorie")
@CrossOrigin
public class CategorieController {

    @Autowired
    private final CategorieService cs;

    public CategorieController(CategorieService cs) {
        this.cs = cs;
    }

    @GetMapping
    public List<Categorie> listCategorie() {
        return cs.consulterCategorie();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable("id") int id) {
        Categorie article = cs.consulterCategorieParId(id);
        if (article != null) {
            return ResponseEntity.ok(article);
        }else {
            return ResponseEntity.accepted().build();
        }
    }

    @PostMapping("/add")
    public void addCategorie(@RequestBody @Valid Categorie categorie) {
        String reponse = cs.creerCategorie(categorie);
        if(reponse.equals("OK")) {
            new ResponseStatusException(HttpStatus.OK, "Catégorie ajoutée");
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cette catégorie existe déjà !");
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCategorie(@PathVariable Long id) {
        cs.supprimerCategorie(id);
        return "redirect:/";
    }
}
