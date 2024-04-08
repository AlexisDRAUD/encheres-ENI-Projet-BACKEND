package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import fr.eni.ecole.enchereseniprojetbackend.bo.Categorie;

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

    @ManyToOne
    private Categorie categorie;

    @ManyToOne
    private Utilisateur vendeur;

    @ManyToOne
    private Utilisateur acheteur;

    @ManyToOne
    private Retrait retrait;

    public Article(String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin, long miseAPrix, EtatVente prixVente, Categorie categorie, Utilisateur vendeur, Retrait retrait) {
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.categorie = categorie;
        this.vendeur = vendeur;
        this.retrait = retrait;
    }

    public Article(String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin, long miseAPrix, EtatVente prixVente, Categorie categorie, Utilisateur vendeur, Utilisateur acheteur, Retrait retrait) {
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.categorie = categorie;
        this.vendeur = vendeur;
        this.acheteur = acheteur;
        this.retrait = retrait;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
