package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.EnchereFormInput;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enchere")
@CrossOrigin
public class EnchereController {

    @Autowired
    private final EncheresService es;

    @Autowired
    private final ArticlesService as;

    @Autowired
    private final UtilisateurService us;

    public EnchereController(EncheresService es, ArticlesService as, UtilisateurService us) {
        this.es = es;
        this.as = as;
        this.us = us;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Enchere>> getEnchereByUserId(@PathVariable("id") int id) {
        List<Enchere> liste = es.consulterEncherebyuserID(id);
        if (liste != null && !liste.isEmpty()) {
            return ResponseEntity.ok(liste);
        } else {
            return ResponseEntity.ok(liste);
        }
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<List<Enchere>> getEnchereByArticleId(@PathVariable("id") int id) {
        List<Enchere> liste = es.consulterEncherebyarticleID(id);
        if (liste != null && !liste.isEmpty()) {
            return ResponseEntity.ok(liste);
        } else {
            return ResponseEntity.ok(liste);
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addEnchere(@RequestBody @Valid EnchereFormInput enchereForm) {
        Enchere enchere = toEnchere(enchereForm);
        Map<String, String> errors = new HashMap<>();

        if (enchere.getMontantEnchere() > enchere.getUtilisateur().getCredit()) {
            errors.put("user", "Vous n'avez pas assez de crédit!");
        }
        if (enchere.getArticle().getVendeur().equals(enchere.getUtilisateur())) {
            errors.put("user", "L'utilisateur ne peut pas enchérir sur son propre article!");
        }
        if (enchere.getDateEnchere().isBefore(enchere.getArticle().getDateDebut())) {
            errors.put("user", "L'enchère n'as pas débuté");
        }
        if (enchere.getDateEnchere().isAfter(enchere.getArticle().getDateFin())) {
            errors.put("user", "L'enchère est terminé");
        }
        Enchere highestEnchere = es.getHighestEnchereForArticle(enchere.getArticle().getId());
        if (highestEnchere != null && enchere.getMontantEnchere() <= highestEnchere.getMontantEnchere()) {
            errors.put("montant", "Le montant de l'enchère doit être supérieur au montant actuel le plus élevé.");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                es.creerEnchere(enchere);
                Article article = enchere.getArticle();
                article.setAcheteur(enchere.getUtilisateur());
                article.setPrixVente(enchere.getMontantEnchere());
                as.editArticle(article);
                return ResponseEntity.ok("Enchère créée avec succès");
            } catch (Exception e) {
                return ResponseEntity.ok("La création de l'enchère a échoué");
            }
        }
    }


    public Enchere toEnchere(EnchereFormInput enchereForm) {
        return new Enchere(
                enchereForm.getDateEnchere(),
                (int) enchereForm.getMontantEnchere(),
                us.getUserById(enchereForm.getUserId()),
                as.consulterArticleParId(enchereForm.getArticleId())
        );
    }
}
