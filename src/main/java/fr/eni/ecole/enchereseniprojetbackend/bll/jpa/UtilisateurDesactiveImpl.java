package fr.eni.ecole.enchereseniprojetbackend.bll.jpa;

import fr.eni.ecole.enchereseniprojetbackend.bll.UtilisateurService;
import fr.eni.ecole.enchereseniprojetbackend.bll.mock.UtilisateurDesactiveService;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import fr.eni.ecole.enchereseniprojetbackend.bo.UtilisateurDesactive;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurDesactiveRepository;
import fr.eni.ecole.enchereseniprojetbackend.dal.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurDesactiveImpl implements UtilisateurDesactiveService {

    @Autowired
    UtilisateurDesactiveRepository udr;

    @Autowired
    UtilisateurRepository ur;

    @Autowired
    UtilisateurService us;

    @Override
    public List<UtilisateurDesactive> getUsers() {
        return udr.findAll();
    }

    @Override
    public UtilisateurDesactive getUserDesactiveById(long id) {
        Optional<UtilisateurDesactive> ud = udr.findById(id);
        if (ud.isPresent()) {
            return ud.get();
        } else {
            return null;
        }
    }

    @Override
    public void addUserDesactive(Utilisateur utilisateur) {
        Long id = utilisateur.getId();
        UtilisateurDesactive ud = mapUtiltoUtilDesactive(utilisateur);
        udr.save(ud);
        ur.deleteById(id);
    }

    @Override
    public void userReactive(Utilisateur utilisateur) {
        ur.save(utilisateur);
    }

    @Override
    public void deleteUserDesactiveById(long id) {
        Optional<UtilisateurDesactive> ud = udr.findById(id);
        if (ud.isPresent()) {
            udr.delete(ud.get());
        }

    }

    private UtilisateurDesactive   mapUtiltoUtilDesactive(Utilisateur utilisateur) {
        UtilisateurDesactive ud = new UtilisateurDesactive(
                utilisateur.getUsername(),
                utilisateur.getPrenom(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getTelephone(),
                utilisateur.getRue(),
                utilisateur.getCodePostal(),
                utilisateur.getVille(),
                utilisateur.getPassword(),
                utilisateur.getCredit(),
                utilisateur.isAdministrateur()
                );
        return ud;
    }
}
