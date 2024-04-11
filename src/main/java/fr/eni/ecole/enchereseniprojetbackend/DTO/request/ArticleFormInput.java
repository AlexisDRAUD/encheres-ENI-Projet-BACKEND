package fr.eni.ecole.enchereseniprojetbackend.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ArticleFormInput {

    @NotBlank(message = "Le nom de l'article est obligatoire")
    private String nomArticle;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull
    private LocalDateTime dateDebut;

    @NotNull
    private LocalDateTime dateFin;

    @Positive
    private Long miseAPrix;

    @Positive
    private Long prixVente;

    @NotNull
    private Long categorieId;

    @NotNull
    private Long vendeurId;

    @NotNull
    @NotBlank(message = "La rue est obligatoire")
    private String rue;

    @NotNull
    @PositiveOrZero
    private Integer codePostal;

    @NotNull
    @NotBlank(message = "La ville est obligatoire")
    private String ville;

}
