package fr.eni.ecole.enchereseniprojetbackend.bll.mock;

import com.github.javafaker.Faker;
import fr.eni.ecole.enchereseniprojetbackend.bo.*;
import fr.eni.ecole.enchereseniprojetbackend.dal.*;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        Faker faker = new Faker(new Locale("fr"));

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

        //Catégorie
        Categorie cat1 = new Categorie("Informatique");
        Categorie cat2 = new Categorie("Ameublement");
        Categorie cat3 = new Categorie("Vêtement");
        Categorie cat4 = new Categorie("Sport & Loisirs");
        Categorie cat5 = new Categorie("Autre");
        List<Categorie> catList = new ArrayList<>();
        catList.add(cat1);
        catList.add(cat2);
        catList.add(cat3);
        catList.add(cat4);
        catList.add(cat5);
        cr.saveAll(catList);

        //Utilisateurs
        List<Utilisateur> userList = new ArrayList<>();

        Utilisateur u1 = new Utilisateur("admin","Alexis", "Draud", "enchere.app@outlook.fr", "0504030201", "rue de l'admin",
                "79000", "Niort", encoder.encode("password"), 500, true);
        userList.add(u1);
        ur.save(u1);

        for (int i =0; i<20 ;i++) {
            Utilisateur u = new Utilisateur(faker.name().username(),faker.name().firstName(), faker.name().lastName(),
                    faker.internet().emailAddress(), faker.phoneNumber().phoneNumber(),
                    faker.address().streetAddressNumber() + faker.address().streetAddress(),
                    faker.address().zipCode(), faker.address().city(), encoder.encode("password"),
                    500, false);
            userList.add(u);
            ur.save(u);
        }

        //Retrait
        List<Retrait> retraitList = new ArrayList<>();

        for (int i =0; i<20 ;i++) {
            Retrait r = new Retrait(faker.address().streetAddressNumber()+faker.address().streetAddress(),
                    faker.address().zipCode(),faker.address().city());
            retraitList.add(r);
            rr.save(r);
        }

        //Article
        List<Article> artList = new ArrayList<>();
        Article a1 = new Article("Guitare électrique", "Guitare électrique voodoo, seconde main avec house",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                100, 100,  cat4, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/guitare.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a1);
        ar.save(a1);

        Article a2 = new Article("Ordinateur HP", "17 pouces, très bon état, HDMI, lecteur CD/DVD, subwoofer intégré, ports USB.",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                220, 220,  cat1, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/ordiHP.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a2);
        ar.save(a2);


        Article a3 = new Article("Xiaomi Redmi A2 32G", "Neuf, jamais ouvert.\n" +
                "\n" +
                "A été envoyé par un opération car le nôtre était cassé. Nous en avons commandé un autre en parallèle donc celui-ci ne nous est plus d'aucun utilité.",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                60, 60,  cat1, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/XiaomiA2.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a3);
        ar.save(a3);

        Article a4 = new Article("Bmx", "Bmx btwin pour enfant, freins fonctionnels, cales pieds, selle rétractable, guidon en acier.",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                60, 60,  cat4, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/bmx.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a4);
        ar.save(a4);

        Article a5 = new Article("Voiture majorette limousine avec toit ouvrant n°339 echelle 1/58", "VOITURE MAJORETTE LIMOUSINE AVEC TOIT OUVRANT N°339 ECHELLE 1/58",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                12, 12,  cat4, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/majorette.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a5);
        ar.save(a5);


        Article a6 = new Article("One pièce figurine dxf brotherhood 2", "Figurine one piece dxf brotherhood 2\n" +
                "Toujours très bien entretenu officiel acheté à la Japan Expo\n" +
                "Attention le sabo a une main en moins je l’ai acheté comme cela à la Japan Expo",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                150, 150,  cat4, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/onepiece.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a6);
        ar.save(a6);


        Article a7 = new Article("Chaussures homme randonnée quechoua décathlon", "CHAUSSURES RANDONNÉE\n" +
                "QUECHOUA\n" +
                "DÉCATHLON\n" +
                "Taille 42/43\n" +
                "Très peu servi",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                30, 30,  cat3, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/chaussureQuechua.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a7);
        ar.save(a7);


        Article a8 = new Article("Pompe filtration piscine Bestway", "Pompe filtration à sable ou balle de filtration" +
                " fourni avec. En très bon état,utilisé que deux mois.",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                120, 120,  cat5, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/pompeFiltre.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a8);
        ar.save(a8);


        Article a9 = new Article("185 kg de poids olympiques en fonte", "185 kg de poids diamètre olympique. Fonte. " +
                "pas abimés,\n" +
                "---Détail : 2 poids de 20 kg\n" +
                "4 poids de 15 kg\n" +
                "6 poids de 10 kg\n" +
                "4 poids de 5 kg\n" +
                "2 poids de 2,5 kg",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                300, 300,  cat4, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/poidFonte.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a9);
        ar.save(a9);

        Article a10 = new Article("Galaxy tab A9 128 giga 4g", "Produit très bonne état général\n" +
                "Fonctionne parfaitement",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                200, 200,  cat1, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/galaxytapa9.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a10);
        ar.save(a10);


        Article a11 = new Article("Pencil ipad Apple", "Pencil Apple 2nde génération\n" +
                "État neuf\n" +
                "Utilise 2 fois mais je ne m’y fait pas….\n" +
                "Toujours dans sa boîte avec les protections et mode d’emploi",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                90, 90,  cat1, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/applePencil.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a11);
        ar.save(a11);


        Article a12 = new Article("Opel Adam 1.4 Twinport 87ch Unlimited Start/Stop", "Opel Adam 1.4 Twinport 87ch Unlimited Start/Stop, Gris, 5cv, 3 portes, mise en circulation le 16-10-2018, première main, garantie 12 mois (Spoticar-Premium 12 Mois).\n" +
                "\n" +
                "4 places, couleur intérieur : Inconu, longueur : 3,70 mètres, poids en charge 1480 Kg,\n" +
                "boîte de vitesse : manuelle\n" +
                "\n" +
                "Kilométrage non garanti.",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                9990, 9990,  cat5, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/voitureOpel.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a12);
        ar.save(a12);


        Article a13 = new Article("Assiettes", "12 petites assiettes\n" +
                "12 creuses\n" +
                "24 grandes",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                50, 50,  cat2, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/assiettes.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a13);
        ar.save(a13);


        Article a14 = new Article("Poissons boucles d’oreilles dorées pour femme", "Poissons boucles d’oreilles dorées en perles pour femme\n" +
                "Doré/blanc/vert",
                LocalDateTime.of(2024, 4, 16, 10, 0),
                LocalDateTime.of(2024, 4, 30, 10, 0),
                10, 10,  cat3, userList.get(faker.number().numberBetween(0, userList.size()-1)),
                "https://enchere-app.osc-fr1.scalingo.io/upload/boucleOreille.jpg", retraitList.get(faker.number().numberBetween(0,
                userList.size()-1)));
        artList.add(a14);
        ar.save(a14);


        //Encheres
        artList.forEach(article -> {
            int nbBoucle = faker.number().numberBetween(0, 10);
            for(int i = 0; i < nbBoucle; i++) {
                Enchere e = new Enchere(LocalDateTime.now().plusMinutes(i),
                        article.getPrixVente()+faker.number().numberBetween(1,100),
                        userList.get(faker.number().numberBetween(0, userList.size()-1)), article);
                article.setPrixVente(e.getMontantEnchere());
                ar.save(article);
                er.save(e);
            }
        });

    }

}
