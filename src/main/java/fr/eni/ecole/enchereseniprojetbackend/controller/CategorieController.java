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
    private CategorieService cs;
/*
    public CategorieController(CategorieService cs) {
        this.cs = cs;
    }
*/
    @GetMapping
    public List<Categorie> listCategorie() {
        return cs.consulterCategorie();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategorieById(@PathVariable("id") long id) {
        try {
            Categorie categorie = cs.consulterCategorieParId(id);
            return ResponseEntity.ok(categorie);
        }catch (ResponseStatusException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
    }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategorie(@RequestBody @Valid Categorie categorie) {
        try {
            cs.creerCategorie(categorie);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur lors de l'insertion : " + error.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategorie(@RequestBody @PathVariable Long id) {
        try {
            cs.supprimerCategorie(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException error) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error.getMessage());
        }
    }
}
