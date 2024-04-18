package fr.eni.ecole.enchereseniprojetbackend.bo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Utilisateur.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Utilisateur user;

    private LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION);

    public PasswordResetToken() {}

    public PasswordResetToken(String token, Utilisateur user) {
        this.token = token;
        this.user = user;
    }
}