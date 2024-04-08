package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Enchere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateEnchère;

    private Long montant_enchere;

    @ManyToOne
    private Utilisateur utilisateur;
    @ManyToOne
    private Article article;
}
