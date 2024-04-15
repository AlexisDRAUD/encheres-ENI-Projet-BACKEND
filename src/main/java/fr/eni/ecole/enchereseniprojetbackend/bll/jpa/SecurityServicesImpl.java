package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bo.PasswordResetToken;
import fr.eni.ecole.enchereseniprojetbackend.bll.SecurityService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.dal.PasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SecurityServicesImpl implements SecurityService {

    @Autowired
    PasswordTokenRepository ptr;

    @Override
    public Utilisateur getUserByPasswordResetToken(String token) {
        final PasswordResetToken passToken = ptr.findByToken(token);

        return passToken.getUser();
    }

    @Override
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = ptr.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return passToken.getExpiryDate().isBefore(LocalDateTime.now());
    }
}
