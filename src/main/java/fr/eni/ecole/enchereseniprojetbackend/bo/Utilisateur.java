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

}
