package fr.eni.ecole.enchereseniprojetbackend.controller;

import fr.eni.ecole.enchereseniprojetbackend.DTO.response.UserPayload;
import fr.eni.ecole.enchereseniprojetbackend.Security.JwtUtils;
import fr.eni.ecole.enchereseniprojetbackend.Security.MyUserDetailsService;
import fr.eni.ecole.enchereseniprojetbackend.Security.UtilisateurSpringSecurity;
import fr.eni.ecole.enchereseniprojetbackend.bll.ArticlesService;
import fr.eni.ecole.enchereseniprojetbackend.bll.EncheresService;
import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import fr.eni.ecole.enchereseniprojetbackend.bo.Article;
import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UtilisateurController {

    @Autowired
    UtilisateurService us;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private EncheresService encheresService;



    @GetMapping("/{id}")
    public UserPayload getUserById(@PathVariable("id") Long id) {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().getId() == id)
        {
            return us.getUserById(id).toUserPayload();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@RequestBody @Valid UserFormInput userForm, BindingResult br,
                                     @PathVariable("id") Long id) {
        Map<String, String> errors = new HashMap<>();

        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().getId() == id)
        {
            if (us.getUserById(id) != null) {
                if (br.hasErrors()) {
                    for (FieldError error : br.getFieldErrors()) {
                        errors.put(error.getField(), error.getDefaultMessage());
                    }
                    return ResponseEntity.badRequest().body(errors);
                }

                if (us.usernameAlreadyExist(userForm.getUsername()) && !userForm.getUsername().equals(userDetails.getUsername())) {
                    errors.put("username", "Username is already taken!");
                }

                if (us.emailAlreadyExist(userForm.getEmail()) && !userForm.getEmail().equals(userDetails.getEmail())) {
                    errors.put("email", "Email is already in use!");
                }

                if (userForm.getPassword() != null && !userForm.getPassword().isBlank()) {
                    if (userForm.getOldPassword() == null || userForm.getOldPassword().isBlank() || !us.isValidOldPassword(userForm.getOldPassword(),
                            us.getUserById(id).getPassword())) {
                        errors.put("oldPassword", "Actual password is incorrect!");
                    }

                    if (userForm.getPassword().length() >= 6 && userForm.getPassword().length() <= 30) {
                        if (!us.isValidPassword(userForm.getPassword(), userForm.getPasswordConfirmation())) {
                            errors.put("password", "Passwords do not match!");
                        }
                    } else {
                        errors.put("password", "Le taille du mot de passe doit être compris entre 6 et 30!");
                    }
                }

                if (!errors.isEmpty()) {
                    return ResponseEntity
                            .badRequest()
                            .body(errors);
                }

                us.updateUser(userForm, id);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }

        return ResponseEntity.ok().body("L'utilisateur a été modifié.");
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().getId() == id || userDetails.getUtilisateur().isAdministrateur())
        {
            if (us.getUserById(id) != null) {
                //Liste de tous les articles
                List<Article> articles =  articlesService.consulterArticle();
                System.out.println(articles);
                //Liste des articles à supprimer
                List<Article> articlesUtilisateur = articles.stream().filter(article -> article.getVendeur().getId() == id).toList();
                System.out.println(articlesUtilisateur);
                if (!articlesUtilisateur.isEmpty()) {
                    for(Article article : articlesUtilisateur){
                        if(article.getAcheteur() != null) {
                            //liste des encheres coorespondant à l'article
                            List<Enchere> ventesEnCours = encheresService.consulterEncherebyarticleID(article.getId());
                            System.out.println("ventes En Cours : " + ventesEnCours);

                            //on recrédite le compte de l'acheteur de la dernière enchère
                            Enchere derniereEnchere = ventesEnCours.get(ventesEnCours.size() - 1);
                            System.out.println("derniereEnchere : " + derniereEnchere);

                            System.out.println("credit avant : " + derniereEnchere.getUtilisateur().getCredit());
                            derniereEnchere.getUtilisateur().setCredit(derniereEnchere.getUtilisateur().getCredit() + derniereEnchere.getMontantEnchere());
                            System.out.println("credit après : " + derniereEnchere.getUtilisateur().getCredit());

                            //suppression de ttes les encheres de l'article
                            for (Enchere enchere : ventesEnCours) {
                                System.out.println("suppression de ttes les encheres de l'article");
                                encheresService.supprimerEnchere(enchere.getId());
                            }
                        }
                        System.out.println("suppression article un par un");
                        articlesService.supprimerArticle(article.getId());
                    }
                }
                List<Enchere> mesEncheres = encheresService.consulterEncherebyuserID(id);
                System.out.println("mesEnchere : " + mesEncheres);

                if(!mesEncheres.isEmpty()){
                    //on récupère un tableau sans doublon des id des articles concernés
                    Set<Long> idArticles = mesEncheres.stream().map(e -> e.getArticle().getId()).collect(Collectors.toSet());
                    System.out.println("idArticles : " + idArticles);
                    for(Long idArt : idArticles){
                        System.out.println("idArt : " + idArt);
                        // liste des encheres de chaque article sur lesquels l'utilisateur a fait une ou plusieurs encheres
                        List<Enchere> mesEncheresArticles = encheresService.consulterEncherebyarticleID(idArt);
                        System.out.println("mesEncheresArticles : " + mesEncheresArticles);

                        //si dernière enchère à pour acheteur utilisateur
                        if(mesEncheresArticles.get(mesEncheresArticles.size()-1).getUtilisateur().getId() == id){

                            if(mesEncheresArticles.size() != 1) {
                                Boolean stop = false;
                                //on cherche l'enchere précédente, on vérifie si le compte utilisateur > montant enchere précédente
                                // et si ce n'est pas une enchere avec acheteur = utilisateur
                                int i = 2;
                                while(!stop && i <= mesEncheresArticles.size()){
                                    System.out.println("While 1 et i :" + i + " " + stop);
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
                                                us.getUserById(mesEncheresArticles.get(mesEncheresArticles.size()-i).getUtilisateur().getId())
                                        );
                                        //montant de cette enchère devient le nouveau prix de vente
                                        mesEncheresArticles.get(mesEncheresArticles.size()-i).getArticle()
                                                .setPrixVente(
                                                        mesEncheresArticles.get(mesEncheresArticles.size()-i).getMontantEnchere()
                                                );
                                        stop = true;
                                        System.out.println("Article a modifier" + mesEncheresArticles.get(mesEncheresArticles.size()-i).getArticle());
                                    } else {
                                        System.out.println("Pas utilise essai 1");
                                        //je supprime cette enchère
                                        encheresService.supprimerEnchere(mesEncheresArticles.get(mesEncheresArticles.size()-i).getId());
                                        i +=1;
                                        //si il ne reste plus d'encheres, on maj l'article
                                        if(i > mesEncheresArticles.size()){
                                            System.out.println("Pas utilise essai 2");
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
                        System.out.println("suppression des encheres de l'utilisateur");
                        if(!encheresForDelete.isEmpty()){
                        //if(!mesEncheresArticles.isEmpty()){
                            for(Enchere enchere : encheresForDelete){
                                encheresService.supprimerEnchere(enchere.getId());
                            }
                        }
                    }
                }
                System.out.println("je peux supprimer");
                us.deleteUserById(id);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }
    }
}

