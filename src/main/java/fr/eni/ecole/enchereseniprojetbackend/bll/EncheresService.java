package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;

import java.util.List;

public interface EncheresService {
    List<Enchere> consulterEncherebyuserID(long id);

    List<Enchere> consulterEncherebyarticleID(long id);

    Enchere getHighestEnchereForArticle(long id);

    void creerEnchere(Enchere enchere) throws Exception;


    void editEnchere(Enchere enchere);

    String supprimerEnchere(long id);

}
