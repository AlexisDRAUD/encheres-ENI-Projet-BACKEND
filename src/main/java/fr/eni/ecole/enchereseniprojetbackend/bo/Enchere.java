package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Enchere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateEnchere;

    private long montantEnchere;

    @ManyToOne
    private Utilisateur utilisateur;
    @ManyToOne
    private Article article;

    public Enchere(LocalDateTime dateEnchere, long montantEnchere, Utilisateur utilisateur, Article article) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.utilisateur = utilisateur;
        this.article = article;
    }
}
