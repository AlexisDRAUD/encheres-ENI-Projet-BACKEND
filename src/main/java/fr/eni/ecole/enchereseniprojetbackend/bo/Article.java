package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Article {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nomArticle;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private long miseAPrix;
    private EtatVente prixVente;

    private Categorie categorie;
    private Utilisateur vendeur;
    private Utilisateur acheteur;
    private List<Enchere> encheres;
    private Retrait retrait;


}
