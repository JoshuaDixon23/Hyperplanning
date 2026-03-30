package fr.univtln.projet.planning.modele.person;

import fr.univtln.projet.planning.entity.TextTransformation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "User") // Toujours conseillé d'éviter "User" qui est un mot-clé SQL
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    protected String emailUniv;

    @Column(nullable = true)
    private String hashedPassword;

    @Column(nullable = false)
    private boolean passwordDefined;

    //@Transient
    //protected static final EmailCreate functionUnivMail = new EmailCreate();

    protected User() {
        this.passwordDefined = false;
    }

    protected User(String firstName, String lastName, String emailUniv) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailUniv = emailUniv;
        this.passwordDefined = false;
    }

    /**
     * Définit le mot de passe (hashedPassword doit déjà être hashé)
     * Cette méthode est utilisée par le repository uniquement
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        this.passwordDefined = true;
    }

    /**
     * Retourne UNIQUEMENT si un mot de passe est défini
     * Ne retourne PAS le hash du mot de passe
     */
    public String getHashedPasswordForAuthentication() {
        return this.hashedPassword;
    }

    /*
    protected static <T extends User> T UserFactory(String fname, String lname, BiFunction<String, String, T> constructor) {
        String formattedFname = TextTransformation.capitalize(fname);
        String formattedLname = lname.toUpperCase();

        T user = constructor.apply(formattedFname, formattedLname);
        user.setEmailUniv(functionUnivMail.apply(user));

        return user;
    }
     */

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailUniv='" + emailUniv + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(emailUniv, user.emailUniv);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(emailUniv);
    }
}