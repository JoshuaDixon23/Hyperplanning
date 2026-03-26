package fr.univtln.projet.planning.entity.authentication;

import fr.univtln.projet.planning.entity.TextTransformation;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthenticationEntity {
    private final String email;
    private String hashedPassword;
    private boolean passwordDefined;
    private final LocalDateTime createdAt;

    public AuthenticationEntity(String email) {
        this.email = email;
        this.passwordDefined = false;
        this.createdAt = LocalDateTime.now();
    }

    public AuthenticationEntity(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.passwordDefined = true;
        this.createdAt = LocalDateTime.now();
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        this.passwordDefined = true;
    }


    @Override
    public String toString() {
        return "AuthenticationEntity{" +
                "email='" + email + '\'' +
                ", passwordDefined=" + passwordDefined +
                ", createdAt=" + createdAt +
                '}';
    }
}

