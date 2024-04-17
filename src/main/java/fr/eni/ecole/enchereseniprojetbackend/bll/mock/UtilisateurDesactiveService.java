package fr.eni.ecole.enchereseniprojetbackend.bll.mock;

import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.bo.UtilisateurDesactive;

import java.util.List;

public interface UtilisateurDesactiveService {

    List<UtilisateurDesactive> getUsers();

    UtilisateurDesactive getUserDesactiveById(long id);

    void addUserDesactive(Utilisateur utilisateur);

    void userReactive(Utilisateur utilisateur);

    void deleteUserDesactiveById(long id);
}
