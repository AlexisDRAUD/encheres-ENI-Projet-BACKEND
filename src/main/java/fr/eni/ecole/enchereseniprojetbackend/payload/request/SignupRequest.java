package fr.eni.ecole.enchereseniprojetbackend.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

public class SignupRequest {
    @Getter
    @Setter
    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Getter
    @Setter
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @Getter
    @Setter
    @NotBlank
    @Size(min = 6, max = 40)
    private String passwordConfirmation;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 30)
    private String nom;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 30)
    private String prenom;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 30)
    private String telephone;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 50)
    private String rue;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 50)
    private String ville;

    @Getter
    @Setter
    @Positive
    @Max(99999)
    private int codePostal;

}