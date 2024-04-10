package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.dal.RetraitRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurRepository;
import fr.eni.ecole.enchereseniprojetbackend.payload.request.UserFormRequest;
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
    PasswordEncoder encoder;

    @Override
    public List<Utilisateur> getUsers() {
        return ur.findAll();
    }

    @Override
    public Utilisateur getUserByPseudoOrEmail(String username) {
        Utilisateur user = ur.findByPseudo(username);
        if (user == null) {
            user = ur.findByEmail(username);
        }
        return user;
    }

    @Override
    public Utilisateur getUserById(long id) {
        return ur.findById(id);
    }

    @Override
    public void addUser(UserFormRequest userForm) {
        Utilisateur user = userForm.toUtilisateur();
        user.setCredit(500);
        user.setAdministrateur(false);
        user.setPassword(encoder.encode(user.getPassword()));

        rr.save(user.getAdresse());
        ur.save(user);
    }

    @Override
    public void updateUser(UserFormRequest userForm, long id) {
        Map<String, String> errors = new HashMap<>();

        Utilisateur user = ur.findById(id);
        user.setPseudo(userForm.getUsername());
        user.setNom(userForm.getNom());
        user.setPrenom(userForm.getPrenom());
        user.setEmail(userForm.getEmail());
        user.setTelephone(userForm.getTelephone());
        if (userForm.getPassword() != null && !userForm.getPassword().isBlank()) {
            user.setPassword(encoder.encode(userForm.getPassword()));
        }
        Retrait addresse = user.getAdresse();
        addresse.setRue(userForm.getRue());
        addresse.setVille(userForm.getVille());
        addresse.setCodePostal(userForm.getCodePostal());
        user.setAdresse(addresse);

        rr.save(user.getAdresse());
        ur.save(user);
    }

    @Override
    public void deleteUserById(long id) {
        ur.deleteById(id);
    }

    @Override
    public boolean usernameAlreadyExist(String pseudo) {
        return ur.existsByPseudo(pseudo);
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
