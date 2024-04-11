package fr.eni.ecole.enchereseniprojetbackend.DTO.request;

import fr.eni.ecole.enchereseniprojetbackend.bo.Retrait;
import fr.eni.ecole.enchereseniprojetbackend.bo.Utilisateur;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

public class UserFormInput {
    @Getter
    @Setter
    @NotNull
    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    @Getter
    @Setter
    @NotNull
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Getter
    @Setter
    private String oldPassword;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String passwordConfirmation;

    @Getter
    @Setter
    @NotNull
    @NotBlank
    @Size(max = 30)
    private String nom;

    @Getter
    @Setter
    @NotNull
    @NotBlank
    @Size(max = 30)
    private String prenom;

    @Getter
    @Setter
    @NotNull
    @NotBlank
    @Size(max = 30)
    private String telephone;

    @Getter
    @Setter
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String rue;

    @Getter
    @Setter
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String ville;

    @Getter
    @Setter
    @NotNull
    @NotBlank
    @Size(max = 5)
    private String codePostal;

    public Utilisateur toUtilisateur() {
        return new Utilisateur(
                this.username,
                this.prenom,
                this.nom,
                this.email,
                this.telephone,
                this.rue,
                this.codePostal,
                this.ville,
                this.password
        );
    }

}