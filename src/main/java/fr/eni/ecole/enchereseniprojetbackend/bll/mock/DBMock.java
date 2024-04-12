package fr.eni.ecole.enchereseniprojetbackend.bll.mock;

import fr.eni.ecole.enchereseniprojetbackend.bo.*;
import fr.eni.ecole.enchereseniprojetbackend.dal.*;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Profile("mock")
@Service
public class DBMock {

    final RetraitRepository rr;
    final CategorieRepository cr;
    final EnchereRepository er;
    final ArticleRepository ar;
    final UtilisateurRepository ur;
    final PasswordEncoder encoder;


    public DBMock(RetraitRepository rr, CategorieRepository cr, EnchereRepository er, ArticleRepository ar, UtilisateurRepository ur, PasswordEncoder encoder) {

        this.rr = rr;
        this.cr = cr;
        this.er = er;
        this.ar = ar;
        this.ur = ur;
        this.encoder = encoder;

        er.deleteAll();
        ar.deleteAll();
        ur.deleteAll();
        rr.deleteAll();
        cr.deleteAll();


        Retrait adresse1 = new Retrait("rue du test1", "79000", "Niort");
        Retrait adresse2 = new Retrait("rue du test2", "44000", "Nantes");
        Retrait adresse3 = new Retrait("rue du test3", "75000", "Paris");
        rr.save(adresse1);
        rr.save(adresse2);
        rr.save(adresse3);

        Categorie cat1 = new Categorie("Informatique");
        Categorie cat2 = new Categorie("Ameublement");
        Categorie cat3 = new Categorie("Vêtement");
        Categorie cat4 = new Categorie("Sport & Loisirs");
        cr.save(cat1);
        cr.save(cat2);
        cr.save(cat3);
        cr.save(cat4);

        Utilisateur u1 = new Utilisateur("admin","admin", "admin", "admin@test.com", "0102030405", "rue du test1", "79000", "Niort", encoder.encode("password"), 500, true);
        Utilisateur u2 = new Utilisateur("user1","user1", "user1", "user1@test.com", "0102030405", "rue du test2",
                "79000", "Niort", encoder.encode("password"), 500, false);
        Utilisateur u3 = new Utilisateur("user2","user2", "user2", "user2@test.com", "0102030405", "rue du test3",
                "79000", "Niort", encoder.encode("password"), 500, false);
        ur.save(u1);
        ur.save(u2);
        ur.save(u3);

        Article a1 = new Article("Article1", "Description1", LocalDateTime.of(2024, 4, 8, 10, 0),
                LocalDateTime.of(2024, 4, 13, 10, 0), 100, 100,  cat1, u1, null, adresse1);
        Article a2 = new Article("Article2", "Description2", LocalDateTime.of(2024, 4, 10, 10, 0),
                LocalDateTime.of(2024, 4, 12, 10, 0), 200, 200, cat2, u2, null, adresse2);
        Article a3 = new Article("Article3", "Description3", LocalDateTime.of(2024, 4, 12, 10, 0),
                LocalDateTime.of(2024, 4, 14, 10, 0), 300, 300, cat3, u3, null, adresse3);
        ar.save(a1);
        ar.save(a2);
        ar.save(a3);

        Enchere e1 = new Enchere(LocalDateTime.of(2024, 4, 8, 11, 0), 150, u1, a2);
        Enchere e2 = new Enchere(LocalDateTime.of(2024, 4, 8, 11, 5), 160, u2, a1);
        Enchere e3 = new Enchere(LocalDateTime.of(2024, 4, 8, 11, 10), 170, u1, a2);
        Enchere e4 = new Enchere(LocalDateTime.of(2024, 5, 8, 11, 10), 170, u1, a2);
        ;
        er.save(e1);
        er.save(e2);
        er.save(e3);
        er.save(e4);

    }

}
