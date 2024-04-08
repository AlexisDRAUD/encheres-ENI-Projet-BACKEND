package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nomArticle;

    private String description;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    private long miseAPrix;

    private EtatVente prixVente;

    private Categorie categorie;

    private Utilisateur vendeur;

    private Utilisateur acheteur;

    private List<Enchere> encheres;

    private Retrait retrait;

    public Article(String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin, long miseAPrix, EtatVente prixVente, Categorie categorie, Utilisateur vendeur, List<Enchere> encheres, Retrait retrait) {
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.categorie = categorie;
        this.vendeur = vendeur;
        this.encheres = encheres;
        this.retrait = retrait;
    }

    public Article(String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin, long miseAPrix, EtatVente prixVente, Categorie categorie, Utilisateur vendeur, Utilisateur acheteur, List<Enchere> encheres, Retrait retrait) {
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.categorie = categorie;
        this.vendeur = vendeur;
        this.acheteur = acheteur;
        this.encheres = encheres;
        this.retrait = retrait;
    }
}
