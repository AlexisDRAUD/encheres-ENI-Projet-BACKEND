package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.PasswordDto;
import fr.eni.ecole.enchereseniprojetbackend.bll.SecurityService;
import fr.eni.ecole.enchereseniprojetbackend.bo.PasswordResetToken;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.dal.PasswordTokenRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.RetraitRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurRepository;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    SecurityService ss;

    @Autowired
    UtilisateurRepository ur;

    @Autowired
    RetraitRepository rr;

    @Autowired
    PasswordTokenRepository ptr;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JavaMailSender mailSender;

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
        user.setCredit(500L);
        user.setAdministrateur(false);
        user.setPassword(encoder.encode(user.getPassword()));

        ur.save(user);
    }

    @Override
    public Map<String, String> updateUser(UserFormInput userForm, long id, Map<String, String> errors) {

        errors = checkUserForm(userForm, errors);
        if (userForm.getOldPassword() == null || userForm.getOldPassword().isBlank() || !isValidOldPassword(userForm.getOldPassword(),
                getUserById(id).getPassword())) {
            errors.put("oldPassword", "Actual password is incorrect!");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

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
        return errors;
    }

    @Override
    public Map<String, String> registerUser(UserFormInput userForm, Map<String, String> errors) {

        errors = checkUserForm(userForm, errors);

        if (errors.isEmpty()) {
            addUser(userForm);
        }

        return errors;
    }

    public Map<String, String> checkUserForm(UserFormInput userForm, Map<String, String> errors) {
        if (usernameAlreadyExist(userForm.getUsername())) {
            errors.put("username", "Username is already taken!");
        }

        if (emailAlreadyExist(userForm.getEmail())) {
            errors.put("email", "Email is already in use!");
        }

        errors = checkPassword(userForm.getPassword(), userForm.getPasswordConfirmation(), errors);

        return errors;
    }

    public Map<String, String> checkPassword(String password, String passwordConfirm, Map<String, String> errors) {
        if (password != null) {
            if (!password.isBlank()) {
                if (password.length() > 6 && password.length() < 30) {
                    if (!isValidPassword(password, passwordConfirm)) {
                        errors.put("password", "Passwords do not match!");
                    }
                } else {
                    errors.put("password", "Le taille du mot de passe doit être compris entre 6 et 30!");
                }
            } else {
                errors.put("password", "Le mot de passe ne doit pas être vide!");
            }
        } else {
            errors.put("password", "Le mot de passe ne doit pas être nul!");
        }
        return errors;
    }

    @Override
    public Map<String, String> resetPassword(String email, Map<String, String> errors) {
        Utilisateur user = getUserByEmail(email);
        if (user == null) {
            errors.put("email", "Cet email n'existe pas!");
            return errors;
        }
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        mailSender.send(constructResetTokenEmail(token, user));

        return errors;
    }

    @Override
    public Map<String, String> savePassword(PasswordDto passwordDTO, Map<String, String> errors) {
        String result = ss.validatePasswordResetToken(passwordDTO.getToken());

        if(result != null) {
            errors.put("url", "Ce lien n'est pas valide!");
            return errors;
        }

        Utilisateur user = ss.getUserByPasswordResetToken(passwordDTO.getToken());
        errors = checkPassword(passwordDTO.getPassword(), passwordDTO.getPasswordConfirmation(), errors);

        if (errors.isEmpty()) {
            changeUserPassword(user, passwordDTO.getPassword());
        }

        return errors;
    }

    private SimpleMailMessage constructResetTokenEmail(String token, Utilisateur user) {
        String url = "http://localhost:3000/change-password/" + token;
        String message ="Cliquez sur le lien suivant pour modifier votre mot de passe : ";
        return constructEmail("Lien de changement de mot de passe", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             Utilisateur user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("enchere.app@outlook.fr");
        return email;
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
