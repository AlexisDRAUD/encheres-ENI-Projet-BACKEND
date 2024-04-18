package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.dal.ArticleRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.EnchereRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToIntFunction;

@Service
public class EncheresServicesImpl implements EncheresService {

    final ArticlesService as;

    final EnchereRepository er;

    final UtilisateurRepository ur;

    public EncheresServicesImpl(EnchereRepository er, ArticlesService as, UtilisateurRepository ur) {
        this.er = er;
        this.as = as;
        this.ur = ur;
    }

    @Override
    public List<Enchere> consulterEncherebyuserID(long id) {
        return er.findAllByUtilisateur_id(id);
    }

    @Override
    public List<Enchere> consulterEncherebyarticleID(long id) {
        return er.findAllByArticle_id(id);
    }

    @Override
    public Enchere getHighestEnchereForArticle(long articleId) {
        List<Enchere> encheres = consulterEncherebyarticleID(articleId);

        if (encheres != null && !encheres.isEmpty()) {
            return encheres.stream()
                    .max(Comparator.comparingLong(Enchere::getMontantEnchere))
                    .orElse(null);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> creerEnchere(Enchere enchere, Map<String, String> errors) throws Exception {

        errors = checkEnchere(enchere, errors);
        if (!errors.isEmpty()) {
            return errors;
        }

        Enchere lastEnchere = getHighestEnchereForArticle(enchere.getArticle().getId());
        if (lastEnchere != null) {
            Utilisateur lastUser = lastEnchere.getUtilisateur();
            lastUser.setCredit(lastUser.getCredit()+lastEnchere.getMontantEnchere());
            ur.save(lastUser);
        }
        Utilisateur newUser = enchere.getUtilisateur();
        newUser.setCredit(newUser.getCredit()-enchere.getMontantEnchere());
        ur.save(newUser);

        er.save(enchere);

        Article article = enchere.getArticle();
        article.setAcheteur(enchere.getUtilisateur());
        article.setPrixVente(enchere.getMontantEnchere());
        as.editArticle(article);

        return errors;
    }

    public Map<String, String> checkEnchere(Enchere enchere, Map<String, String> errors) {
        Enchere highestEnchere = getHighestEnchereForArticle(enchere.getArticle().getId());
        if (highestEnchere != null){
            if (highestEnchere.getUtilisateur() == enchere.getUtilisateur() ) {
                errors.put("user", "L'utilisateur ne peut pas sur-enchérir sur sa propre enchère");
            }
            if (highestEnchere != null && enchere.getMontantEnchere() <= highestEnchere.getMontantEnchere()) {
                errors.put("montant", "Le montant de l'enchère doit être supérieur au montant actuel le plus élevé.");
            }
        }
        if (enchere.getMontantEnchere() < enchere.getArticle().getMiseAPrix()) {
            errors.put("montant", "Le montant doit être supérieur au montant de la mise à prix");
        }

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

        return errors;
    }

    @Override
    public void editEnchere(Enchere enchere) {

    }

    @Override
    public String supprimerEnchere(long id) {
        String message = "OK";
        Optional<Enchere> enchere = er.findById(id);
        if (enchere.isPresent()) {
            er.deleteById(id);
        } else {
            message = "Erreur lors de la suppression";
        }
        return message;
    }
}
