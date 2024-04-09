package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.dal.RetraitRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    UtilisateurRepository ur;

    @Autowired
    RetraitRepository rr;

    @Override
    public List<Utilisateur> getUsers() {
        return ur.findAll();
    }

    @Override
    public Utilisateur getUserByPseudoOrEmail(String username) {
        Utilisateur user = ur.findByPseudo(username);
        if (user == null) {
            user = ur.findByEmail(username);
        }
        return user;
    }

    @Override
    public Utilisateur getUserById(long id) {
        return ur.findById(id);
    }

    @Override
    public void addUser(Utilisateur utilisateur) {
        ur.save(utilisateur);
        rr.save(utilisateur.getAdresse());
    }

    @Override
    public void updateUser(Utilisateur utilisateur) {
        ur.save(utilisateur);
        rr.save(utilisateur.getAdresse());
    }

    @Override
    public void deleteUserById(long id) {
        ur.deleteById(id);
    }
}
