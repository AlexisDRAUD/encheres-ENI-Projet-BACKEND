package fr.eni.ecole.enchereseniprojetbackend.bo;

import fr.eni.ecole.enchereseniprojetbackend.DTO.request.UserFormInput;
import fr.eni.ecole.enchereseniprojetbackend.DTO.response.UserPayload;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDesactive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String username;


    private String nom;


    private String prenom;


    private String email;

    private String telephone;

    private String rue;

    private String codePostal;

    private String ville;

    @NotNull
    private String password;

    @PositiveOrZero
    private long credit;

    @NotNull
    private boolean administrateur;

    public UtilisateurDesactive(String username, String prenom, String nom, String email, String telephone, String rue, String codePostal, String ville, String password, long credit, boolean administrateur) {
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

    public UtilisateurDesactive(String username, String prenom, String nom, String email, String telephone, String rue, String codePostal, String ville, String password) {
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
