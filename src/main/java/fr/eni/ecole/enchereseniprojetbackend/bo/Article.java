package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    private Long id;

    @NotBlank(message = "Le nom de l'article est obligatoire")
    private String nomArticle;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 2500)
    @Column(length = 2500)
    private String description;

    @NotNull
    private LocalDateTime dateDebut;

    @NotNull
    private LocalDateTime dateFin;

    @Positive
    private Long miseAPrix;

    @Positive
    private Long prixVente;

    @ManyToOne
    private Categorie categorie;

    @ManyToOne
    private Utilisateur vendeur;

    @ManyToOne
    private Utilisateur acheteur;

    @ManyToOne
    private Retrait retrait;

    private String img;

    public Article(String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin,
                   long miseAPrix, long prixVente, Categorie categorie, Utilisateur vendeur , String img,
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
        this.img = img;
    }

    public Article(String nomArticle, String description, LocalDateTime dateDebut, LocalDateTime dateFin,
                   long miseAPrix, long prixVente, Categorie categorie, Utilisateur vendeur,
                   Utilisateur acheteur, String img,
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
        this.img = img;
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
