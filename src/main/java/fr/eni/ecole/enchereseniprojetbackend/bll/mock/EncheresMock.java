package fr.eni.ecole.enchereseniprojetbackend.bll.mock;

import fr.eni.ecole.enchereseniprojetbackend.bll.Encheres;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;

import java.util.List;

public class EncheresMock implements Encheres {
    @Override
    public List<Enchere> consulterEnchere() {
        return null;
    }

    @Override
    public Enchere consulterEnchereParId(long id) {
        return null;
    }

    @Override
    public void creerEnchere(Enchere enchere) {

    }

    @Override
    public void editEnchere(Enchere enchere) {

    }

    @Override
    public void supprimerEnchere(long id) {

    }
}
