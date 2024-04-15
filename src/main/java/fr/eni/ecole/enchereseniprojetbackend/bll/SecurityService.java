package fr.eni.ecole.enchereseniprojetbackend.bll;

import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;

public interface SecurityService {
    String validatePasswordResetToken(String token);

    Utilisateur getUserByPasswordResetToken(String token);
}
