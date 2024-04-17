package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;

import java.util.List;
import java.util.Map;

public interface EncheresService {
    List<Enchere> consulterEncherebyuserID(long id);

    List<Enchere> consulterEncherebyarticleID(long id);

    Enchere getHighestEnchereForArticle(long id);

    Map<String, String> creerEnchere(Enchere enchere, Map<String, String> errors) throws Exception;

    void editEnchere(Enchere enchere);

    String supprimerEnchere(long id);

}
