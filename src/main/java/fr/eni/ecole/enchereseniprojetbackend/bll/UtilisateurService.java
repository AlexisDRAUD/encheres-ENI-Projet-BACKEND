package fr.eni.ecole.enchereseniprojetbackend.bll;

import java.util.List;
import java.util.Map;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.PasswordDto;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import org.springframework.web.server.ResponseStatusException;

public interface UtilisateurService {

    List<Utilisateur> getUsers();

    Utilisateur getUserByUsernameOrEmail(String username);

    Utilisateur getUserByEmail(String email);

    Utilisateur getUserById(long id);

    void addUser(UserFormInput userForm);

    Map<String, String> updateUser(UserFormInput userForm, long id, Map<String, String> errors);

    Map<String, String> registerUser(UserFormInput userForm, Map<String, String> errors);

    Map<String, String> resetPassword(String email, Map<String, String> errors);

    Map<String, String> savePassword(PasswordDto passwordDTO, Map<String, String> errors);

    Map<String, String> addMoney(long Userid, Long money , Map<String, String> errors);

    //void deleteUserById(long id);

    /*
        @Override
        public void deleteUserById(long id) {
            ur.deleteById(id);
        }
    */
    void deleteUserById(long id, Boolean isForDisabled) throws ResponseStatusException;

    boolean usernameAlreadyExist(String username);

    boolean emailAlreadyExist(String email);

    boolean isValidOldPassword(String oldPassword, String actualPassword);

    boolean isValidPassword(String password, String passwordConfirmation);

    void createPasswordResetTokenForUser(Utilisateur user, String token);

    void changeUserPassword(Utilisateur user, String password);



}
