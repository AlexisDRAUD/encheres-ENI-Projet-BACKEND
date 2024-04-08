package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank(message = "Le pseudo est obligatoire")
    private String pseudo;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private String telephone;

    @OneToOne
    private Retrait adresse;

    @NotNull
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @PositiveOrZero
    private long credit;

    @NotNull
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
