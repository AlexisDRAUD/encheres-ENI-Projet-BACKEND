package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Enchere;

import java.util.List;

public interface EncheresService {
    List<Enchere> consulterEnchere();
    Enchere consulterEnchereParId(long id);

    void creerEnchere(Enchere enchere);


    void editEnchere(Enchere enchere);

    void supprimerEnchere(long id);

}
