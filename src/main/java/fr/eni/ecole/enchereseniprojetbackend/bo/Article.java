package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Le nom de l'article est obligatoire")
    private String nomArticle;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull
    private LocalDateTime dateDebut;

    @NotNull
    private LocalDateTime dateFin;

    @Positive
    private long miseAPrix;

    @Positive
    private long prixVente;

    @ManyToOne
    private Categorie categorie;

    @ManyToOne
    private Utilisateur vendeur;

    @ManyToOne
    private Utilisateur acheteur;

    @ManyToOne
    private Retrait retrait;

    public Article(String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin,
                   long miseAPrix, long prixVente, Categorie categorie, Utilisateur vendeur,
                   Retrait retrait) {
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

    public Article(String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin,
                   long miseAPrix, long prixVente, Categorie categorie, Utilisateur vendeur,
                   Utilisateur acheteur,
                   Retrait retrait) {
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

    public long getId() {
        return (int) id;
    }

    public EtatVente getEtatVente() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(dateDebut)) {
            return EtatVente.CREER;
        } else if (now.isAfter(dateDebut) && now.isBefore(dateFin)) {
            return EtatVente.EN_COURS;
        } else {
            return EtatVente.VENDU;
        }
    }
}
