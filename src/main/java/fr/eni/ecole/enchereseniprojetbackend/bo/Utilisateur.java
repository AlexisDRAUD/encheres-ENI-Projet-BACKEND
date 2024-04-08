package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String pseudo;

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    @OneToOne
    private Retrait adresse;

    private String password;

    private long credit;

    private boolean administrateur;

    public Utilisateur(String pseudo, String nom, String email, String telephone, Retrait adresse, String password, long credit, boolean administrateur) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.password = password;
        this.credit = credit;
        this.administrateur = administrateur;
    }
}
