package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;

public interface RetraitService {
    void deleteRetrait(long id);
    Retrait recupererRetraitById(long id);
}
