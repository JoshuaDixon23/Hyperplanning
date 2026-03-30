package fr.univtln.projet.planning.modele.authentication;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Authentication")
@Getter
@Setter
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authenticationId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true)
    private String hashedPassword;

    @Column(nullable = false)
    private boolean passwordDefined;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Authentication() {
    }

    public Authentication(String email) {
        this.email = email;
        this.passwordDefined = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Authentication(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.passwordDefined = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        this.passwordDefined = true;
        this.updatedAt = LocalDateTime.now();
    }


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "authenticationId=" + authenticationId +
                ", email='" + email + '\'' +
                ", passwordDefined=" + passwordDefined +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

