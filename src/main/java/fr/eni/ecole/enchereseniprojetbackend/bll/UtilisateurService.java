package fr.eni.ecole.enchereseniprojetbackend.bll;

import java.util.List;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;

public interface UtilisateurService {

    List<Utilisateur> getUsers();

    Utilisateur getUserByPseudoOrEmail(String username);

    Utilisateur getUserById(long id);

    void addUser(Utilisateur utilisateur);

    void updateUser(Utilisateur utilisateur);

    void deleteUserById(long id);

    boolean usernameAlreadyExist(String pseudo);

    boolean emailAlreadyExist(String email);

}
