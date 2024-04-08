package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Enchere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateEnchère;

    private Long montant_enchere;
}
