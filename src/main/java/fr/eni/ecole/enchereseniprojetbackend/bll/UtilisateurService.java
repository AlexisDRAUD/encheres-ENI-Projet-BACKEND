package fr.eni.ecole.enchereseniprojetbackend.bll;

import java.util.List;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;

public interface UtilisateurService {

    List<Utilisateur> getUsers();

    Utilisateur getUserByUsernameOrEmail(String username);

    Utilisateur getUserByEmail(String email);

    Utilisateur getUserById(long id);

    void addUser(UserFormInput userForm);

    void updateUser(UserFormInput userForm, long id);

    void deleteUserById(long id);

    boolean usernameAlreadyExist(String username);

    boolean emailAlreadyExist(String email);

    boolean isValidOldPassword(String oldPassword, String actualPassword);

    boolean isValidPassword(String password, String passwordConfirmation);

    void createPasswordResetTokenForUser(Utilisateur user, String token);

    void changeUserPassword(Utilisateur user, String password);
}
