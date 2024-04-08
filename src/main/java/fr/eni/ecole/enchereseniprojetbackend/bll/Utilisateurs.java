package fr.eni.ecole.enchereseniprojetbackend.bll;

import java.util.List;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;

public interface Utilisateurs {

    List<Utilisateur> consulterUtilisateurs();
    Utilisateur consulterUtilisateurParId(long id);

    void creerUtilisateur(Utilisateur utilisateur);


    void editUtilisateur(Utilisateur utilisateur);

    void supprimerUtilisateur(long id);
}
