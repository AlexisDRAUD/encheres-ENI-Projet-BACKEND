package fr.eni.ecole.enchereseniprojetbackend.bll;

import java.util.List;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;

public interface UtilisateurService {

    List<Utilisateur> getUsers();

    Utilisateur getUserByPseudoOrEmail(String username);

    Utilisateur getUserById(long id);

    void addUser(UserFormInput userForm);

    void updateUser(UserFormInput userForm, long id);

    void deleteUserById(long id);

    boolean usernameAlreadyExist(String pseudo);

    boolean emailAlreadyExist(String email);

    boolean isValidOldPassword(String oldPassword, String actualPassword);

    boolean isValidPassword(String password, String passwordConfirmation);
}
