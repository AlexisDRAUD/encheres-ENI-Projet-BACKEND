package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bo.PasswordResetToken;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.dal.PasswordTokenRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.RetraitRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurRepository;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    UtilisateurRepository ur;

    @Autowired
    RetraitRepository rr;

    @Autowired
    PasswordTokenRepository ptr;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public List<Utilisateur> getUsers() {
        return ur.findAll();
    }

    @Override
    public Utilisateur getUserByUsernameOrEmail(String username) {
        Utilisateur user = ur.findByUsername(username);
        if (user == null) {
            user = ur.findByEmail(username);
        }
        return user;
    }

    @Override
    public void createPasswordResetTokenForUser(Utilisateur user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        ptr.save(myToken);
    }

    @Override
    public void changeUserPassword(Utilisateur user, String password) {
        ptr.deleteById(ptr.findByUser(user).getId());
        user.setPassword(encoder.encode(password));
        ur.save(user);
    }

    @Override
    public Utilisateur getUserByEmail(String email) {
        return ur.findByEmail(email);
    }

    @Override
    public Utilisateur getUserById(long id) {
        return ur.findById(id);
    }

    @Override
    public void addUser(UserFormInput userForm) {
        Utilisateur user = userForm.toUtilisateur();
        user.setCredit(500);
        user.setAdministrateur(false);
        user.setPassword(encoder.encode(user.getPassword()));

        ur.save(user);
    }

    @Override
    public void updateUser(UserFormInput userForm, long id) {
        Map<String, String> errors = new HashMap<>();

        Utilisateur user = ur.findById(id);
        user.setNom(userForm.getNom());
        user.setPrenom(userForm.getPrenom());
        user.setEmail(userForm.getEmail());
        user.setTelephone(userForm.getTelephone());
        if (userForm.getPassword() != null && !userForm.getPassword().isBlank()) {
            user.setPassword(encoder.encode(userForm.getPassword()));
        }
        user.setRue(userForm.getRue());
        user.setVille(userForm.getVille());
        user.setCodePostal(userForm.getCodePostal());

        ur.save(user);
    }

    @Override
    public void deleteUserById(long id) {
        ur.deleteById(id);
    }

    @Override
    public boolean usernameAlreadyExist(String username) {
        return ur.existsByUsername(username);
    }

    @Override
    public boolean emailAlreadyExist( String email) {
        return ur.existsByEmail(email);
    }

    @Override
    public boolean isValidOldPassword(String oldPassword, String actualPassword) {
        return encoder.matches(oldPassword, actualPassword);
    }

    @Override
    public boolean isValidPassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
}
