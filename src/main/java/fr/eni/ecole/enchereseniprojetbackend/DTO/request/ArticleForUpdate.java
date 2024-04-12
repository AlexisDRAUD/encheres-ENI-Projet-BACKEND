package fr.eni.ecole.enchereseniprojetbackend.DTO.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleForUpdate {

    private Long id;

    private String nomArticle;

    private String description;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    private Long miseAPrix;

    private Long prixVente;

    private Long categorieId;

    private Long vendeurId;

    private String rue;

    private String codePostal;

    private String ville;

    private String img;

    @Override
    public String toString() {
        return "ArticleForUpdate{" +
                "id=" + id +
                ", nomArticle='" + nomArticle + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", miseAPrix=" + miseAPrix +
                ", prixVente=" + prixVente +
                ", categorieId=" + categorieId +
                ", vendeurId=" + vendeurId +
                ", rue='" + rue + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
