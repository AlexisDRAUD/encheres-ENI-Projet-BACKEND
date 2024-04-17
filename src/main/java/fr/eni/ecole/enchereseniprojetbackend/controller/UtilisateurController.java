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
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
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


    @GetMapping()
    public ResponseEntity<List<Utilisateur>> getUsers() {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getUtilisateur().isAdministrateur())
        {
            List<Utilisateur> utilisateurs = us.getUsers();
            if(utilisateurs != null){
                return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

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

                errors = us.updateUser(userForm, id, errors);

                if (!errors.isEmpty()) {
                    return ResponseEntity
                            .badRequest()
                            .body(errors);
                }

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
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        UtilisateurSpringSecurity userDetails =
                (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getUtilisateur().getId() == id || userDetails.getUtilisateur().isAdministrateur())
        {
            if (us.getUserById(id) != null) {
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
                                                us.getUserById(mesEncheresArticles.get(mesEncheresArticles.size()-i).getUtilisateur().getId())
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
                us.deleteUserById(id);
                throw new ResponseStatusException(HttpStatus.OK, "utilisateur supprimé avec succès");
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cet utilisateur n'existe pas !");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous n'avez pas accès a cet utilisateur !");
        }
    }
}

