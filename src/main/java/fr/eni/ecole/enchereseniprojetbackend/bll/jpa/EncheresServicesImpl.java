package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.dal.EnchereRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

@Service
public class EncheresServicesImpl implements EncheresService {

    final EnchereRepository er;

    final UtilisateurRepository ur;

    public EncheresServicesImpl(EnchereRepository er, UtilisateurRepository ur) {
        this.er = er;
        this.ur = ur;
    }

    @Override
    public List<Enchere> consulterEncherebyuserID(int id) {
        return er.findAllByUtilisateur_id(id);
    }

    @Override
    public List<Enchere> consulterEncherebyarticleID(int id) {
        return er.findAllByArticle_id(id);
    }

    @Override
    public Enchere getHighestEnchereForArticle(long articleId) {
        List<Enchere> encheres = consulterEncherebyarticleID((int) articleId);

        if (encheres != null && !encheres.isEmpty()) {
            return encheres.stream()
                    .max(Comparator.comparingInt((ToIntFunction<? super Enchere>) Enchere::getMontantEnchere))
                    .orElse(null);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void creerEnchere(Enchere enchere) throws Exception {

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
    }

    @Override
    public void editEnchere(Enchere enchere) {

    }

    @Override
    public void supprimerEnchere(long id) {

    }
}
