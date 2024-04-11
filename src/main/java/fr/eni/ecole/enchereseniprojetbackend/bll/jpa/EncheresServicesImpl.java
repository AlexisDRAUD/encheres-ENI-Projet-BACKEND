package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import fr.eni.ecole.enchereseniprojetbackend.dal.EnchereRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

@Service
public class EncheresServicesImpl implements EncheresService {

    final EnchereRepository er;

    public EncheresServicesImpl(EnchereRepository er) {
        this.er = er;
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
    public void creerEnchere(Enchere enchere) {
        er.save(enchere);
    }

    @Override
    public void editEnchere(Enchere enchere) {

    }

    @Override
    public void supprimerEnchere(long id) {

    }
}
