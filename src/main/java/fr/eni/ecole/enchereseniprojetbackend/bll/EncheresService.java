package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;

import java.util.List;

public interface EncheresService {
    List<Enchere> consulterEncherebyuserID(int id);

    List<Enchere> consulterEncherebyarticleID(int id);

    void creerEnchere(Enchere enchere);


    void editEnchere(Enchere enchere);

    void supprimerEnchere(long id);

}
