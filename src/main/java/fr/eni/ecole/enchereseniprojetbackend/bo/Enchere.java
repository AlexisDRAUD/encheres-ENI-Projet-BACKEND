package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enchere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dateEnchere;

    @PositiveOrZero
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
