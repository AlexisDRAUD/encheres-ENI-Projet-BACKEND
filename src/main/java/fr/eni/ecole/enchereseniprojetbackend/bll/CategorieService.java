package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;

import java.util.List;

public interface CategorieService {

    List<Categorie> consulterCategorie();
    Categorie consulterCategorieParId(long id);

    String creerCategorie(Categorie categorie);

    void editCategorie(Categorie Categorie);

    void supprimerCategorie(long id);
}
