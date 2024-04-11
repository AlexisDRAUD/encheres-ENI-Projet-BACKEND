package fr.eni.ecole.enchereseniprojetbackend.bo;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormRequest;
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

    public Utilisateur(String pseudo, String prenom, String nom, String email, String telephone, Retrait adresse, String password, long credit, boolean administrateur) {
        this.pseudo = pseudo;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.password = password;
        this.credit = credit;
        this.administrateur = administrateur;
    }

    public Utilisateur(String pseudo, String prenom, String nom, String email, String telephone, Retrait adresse, String password) {
        this.pseudo = pseudo;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.password = password;
    }

    public UserFormRequest toUserForm() {
        UserFormRequest userForm = new UserFormRequest();
        userForm.setUsername(this.pseudo);
        userForm.setNom(this.nom);
        userForm.setPrenom(this.prenom);
        userForm.setEmail(this.email);
        userForm.setTelephone(this.telephone);
        userForm.setRue(this.adresse.getRue());
        userForm.setCodePostal(this.adresse.getCodePostal());
        userForm.setVille(this.adresse.getVille());
        return userForm;
    }
}
