package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurDesactiveService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    SecurityService ss;

    @Autowired
    ArticlesService articlesService;

    @Autowired
    EncheresService encheresService;

    @Autowired
    UtilisateurDesactiveService uds;

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

    @Override
    public Map<String, String> addMoney(long userId, Long money, Map<String, String> errors) {
        Utilisateur u = ur.findById(userId);
        u.setCredit( u.getCredit() + money);
        ur.save(u);
        return errors;
    }


    private SimpleMailMessage constructResetTokenEmail(String token, Utilisateur user) {
        String url = "https://encheres-eni-projet-front.vercel.app/change-password/" + token;
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
/*
    @Override
    public void deleteUserById(long id) {
        ur.deleteById(id);
    }
*/
    @Override
    public void deleteUserById(long id, Boolean isForDisabled) throws ResponseStatusException {

        if (getUserById(id) != null) {
            //Liste de tous les articles
            List<Article> articles =  articlesService.consulterArticle();
            //Liste des articles à supprimer
            List<Article> articlesUtilisateur = articles.stream().filter(article -> article.getVendeur().getId() == id).toList();
            if (!articlesUtilisateur.isEmpty()) {
                for(Article article : articlesUtilisateur){
                    if(article.getAcheteur() != null) {
                        //liste des encheres coorespondant à l'article
                        List<Enchere> ventesEnCours = encheresService.consulterEncherebyarticleID(article.getId());

                        //on recrédite le compte de l'acheteur de la dernière enchère
                        Enchere derniereEnchere = ventesEnCours.get(ventesEnCours.size() - 1);
                        derniereEnchere.getUtilisateur().setCredit(derniereEnchere.getUtilisateur()
                                .getCredit() + derniereEnchere.getMontantEnchere());

                        //suppression de ttes les encheres de l'article
                        for (Enchere enchere : ventesEnCours) {
                            String reponse = encheresService.supprimerEnchere(enchere.getId());
                            if(!reponse.equals("OK")){
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND, reponse);
                            }
                        }
                    }
                    articlesService.supprimerArticle(article.getId());
                }
            }
            //Gestion des encheres à supprimer avec mise à jour
            // des crédits utilisateurs et des articles
            List<Enchere> mesEncheres = encheresService.consulterEncherebyuserID(id);
        if(!mesEncheres.isEmpty()){
            //on récupère un tableau sans doublon des id des articles concernés
            Set<Long> idArticles = mesEncheres.stream().map(e -> e.getArticle().getId()).collect(Collectors.toSet());
            for(Long idArt : idArticles){
                // liste des encheres de chaque article sur lesquels l'utilisateur a fait une ou plusieurs encheres
                List<Enchere> mesEncheresArticles = encheresService.consulterEncherebyarticleID(idArt);

                //si dernière enchère à pour acheteur utilisateur
                if(mesEncheresArticles.get(mesEncheresArticles.size()-1).getUtilisateur().getId() == id){
                    if(mesEncheresArticles.size() != 1) {
                        Boolean stop = false;
                        //on cherche l'enchere précédente, on vérifie si le compte utilisateur > montant enchere précédente
                        // et si ce n'est pas une enchere avec acheteur = utilisateur
                        int i = 2;
                        while(!stop && i <= mesEncheresArticles.size()){
                            if (mesEncheresArticles.get(mesEncheresArticles.size()-i).getUtilisateur().getCredit()
                                    > mesEncheresArticles.get(mesEncheresArticles.size()-i).getMontantEnchere() &&
                                    mesEncheresArticles.get(mesEncheresArticles.size()-i).getUtilisateur().getId() != id
                            ) {
                                //j'enlève le montant de l'enchere sur son compte
                                mesEncheresArticles.get(mesEncheresArticles.size()-i).getUtilisateur()
                                        .setCredit(
                                                mesEncheresArticles.get(mesEncheresArticles.size()-i).getUtilisateur().getCredit()
                                                        - mesEncheresArticles.get(mesEncheresArticles.size()-i).getMontantEnchere()
                                        );
                                //cet utilisateur devient acheteur du produit
                                mesEncheresArticles.get(mesEncheresArticles.size()-i).getArticle().setAcheteur(
                                        getUserById(mesEncheresArticles.get(mesEncheresArticles.size()-i).getUtilisateur().getId())
                                );
                                //montant de cette enchère devient le nouveau prix de vente
                                mesEncheresArticles.get(mesEncheresArticles.size()-i).getArticle()
                                        .setPrixVente(
                                                mesEncheresArticles.get(mesEncheresArticles.size()-i).getMontantEnchere()
                                        );
                                stop = true;
                            } else {
                                //je supprime cette enchère
                                String reponse = encheresService.supprimerEnchere(mesEncheresArticles.get(mesEncheresArticles.size()-i).getId());
                                if(!reponse.equals("OK")){
                                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, reponse);
                                }
                                i +=1;
                                //si il ne reste plus d'encheres, on maj l'article
                                if(i > mesEncheresArticles.size()){
                                    Article art = articlesService.consulterArticleParId(idArt);
                                    art.setPrixVente(art.getMiseAPrix());
                                    art.setAcheteur(null);
                                }
                            }
                        }
                    } else {
                        Article art = articlesService.consulterArticleParId(idArt);
                        art.setPrixVente(art.getMiseAPrix());
                        art.setAcheteur(null);
                    }
                }
                //suppression des encheres de l'utilisateur qui correspondent à l'article
                List<Enchere> encheresForDelete = encheresService.consulterEncherebyuserID(id).stream()
                        .filter(e -> e.getArticle().getId() == idArt).toList();
                if(!encheresForDelete.isEmpty()){
                    for(Enchere enchere : encheresForDelete){
                        String reponse = encheresService.supprimerEnchere(enchere.getId());
                        if(!reponse.equals("OK")){
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, reponse);
                        }
                    }
                }
            }
        }
        if(isForDisabled){
            Utilisateur u = getUserById(id);
            uds.addUserDesactive(u);
            throw new ResponseStatusException(HttpStatus.OK, "utilisateur désactivé avec succès");
        } else {
            ur.deleteById(id);
            throw new ResponseStatusException(HttpStatus.OK, "utilisateur supprimé avec succès");
        }
    }
            else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
    }
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
