package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enchere")
@CrossOrigin
public class EnchereController {

    @Autowired
    private final EncheresService es;

    public EnchereController(EncheresService es) {
        this.es = es;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Enchere>> getEnchereByUserId(@PathVariable("id") int id) {
        List<Enchere> liste = es.consulterEncherebyuserID(id);
        if (liste != null && !liste.isEmpty()) {
            return ResponseEntity.ok(liste);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/article/{id}")
    public ResponseEntity<List<Enchere>> getEnchereByArticleId(@PathVariable("id") int id) {
        List<Enchere> liste = es.consulterEncherebyarticleID(id);
        if (liste != null && !liste.isEmpty()) {
            return ResponseEntity.ok(liste);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/add")
    public String addEnchere(@RequestBody @Valid Enchere enchere) {
        es.creerEnchere(enchere);
        return "redirect:/";
    }

}
