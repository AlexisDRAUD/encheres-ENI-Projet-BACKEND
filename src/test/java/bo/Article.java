package bo;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    private long id;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private long miseAPrix;
    private long prixDeVente;
    private String etatVente;


}
