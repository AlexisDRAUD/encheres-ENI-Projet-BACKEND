package fr.eni.ecole.enchereseniprojetbackend.bll.mock;

import fr.eni.ecole.enchereseniprojetbackend.bll.Utilisateurs;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;

import java.util.List;

public class UtilisateurMock implements Utilisateurs {
    @Override
    public List<Utilisateur> consulterUtilisateurs() {
        return null;
    }

    @Override
    public Utilisateur consulterUtilisateurParId(long id) {
        return null;
    }

    @Override
    public void creerUtilisateur(Utilisateur utilisateur) {

    }

    @Override
    public void editUtilisateur(Utilisateur utilisateur) {

    }

    @Override
    public void supprimerUtilisateur(long id) {

    }
}
