package fr.eni.ecole.enchereseniprojetbackend.bo;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import fr.eni.ecole.enchereseniprojetbackend.DTO.response.UserPayload;
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
    private String username;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private String telephone;

    @NotBlank(message = "La rue est obligatoire")
    private String rue;

    @NotBlank(message = "Le code postal est obligatoire")
    private String codePostal;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @NotNull
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @PositiveOrZero
    private long credit;

    @NotNull
    private boolean administrateur;

    public Utilisateur(String username, String prenom, String nom, String email, String telephone, String rue, String codePostal, String ville, String password, long credit, boolean administrateur) {
        this.username = username;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.password = password;
        this.credit = credit;
        this.administrateur = administrateur;
    }

    public Utilisateur(String username, String prenom, String nom, String email, String telephone, String rue, String codePostal, String ville, String password) {
        this.username = username;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.password = password;
    }

    public UserFormInput toUserForm() {
        UserFormInput userForm = new UserFormInput();
        userForm.setUsername(this.username);
        userForm.setNom(this.nom);
        userForm.setPrenom(this.prenom);
        userForm.setEmail(this.email);
        userForm.setTelephone(this.telephone);
        userForm.setRue(this.getRue());
        userForm.setCodePostal(this.getCodePostal());
        userForm.setVille(this.getVille());
        return userForm;
    }

    public UserPayload toUserPayload() {
        UserPayload userPayload = new UserPayload();
        userPayload.setId(this.id);
        userPayload.setUsername(this.username);
        userPayload.setNom(this.nom);
        userPayload.setPrenom(this.prenom);
        userPayload.setEmail(this.email);
        userPayload.setTelephone(this.telephone);
        userPayload.setRue(this.getRue());
        userPayload.setCodePostal(this.getCodePostal());
        userPayload.setVille(this.getVille());
        userPayload.setCredit(this.credit);
        return userPayload;
    }
}
