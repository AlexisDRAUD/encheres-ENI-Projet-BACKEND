package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;

import java.util.List;

public class UtilisateurServiceImpl implements UtilisateurService {


    @Override
    public List<Utilisateur> getUsers() {
        return List.of();
    }

    @Override
    public Utilisateur getUserById(long id) {
        return null;
    }

    @Override
    public void addUser(Utilisateur utilisateur) {

    }

    @Override
    public void updateUser(Utilisateur utilisateur) {

    }

    @Override
    public void deleteUserById(long id) {

    }
}
