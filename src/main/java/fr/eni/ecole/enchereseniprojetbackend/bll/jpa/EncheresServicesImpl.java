package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import fr.eni.ecole.enchereseniprojetbackend.dal.EnchereRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
