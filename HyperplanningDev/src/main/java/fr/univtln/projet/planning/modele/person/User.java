package fr.univtln.projet.planning.modele.person;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users") // Toujours conseillé d'éviter "User" qui est un mot-clé SQL
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

    //@Transient
    //protected static final EmailCreate functionUnivMail = new EmailCreate();

    protected User() {
    }

    protected User(String firstName, String lastName, String emailUniv) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailUniv = emailUniv;
    }

    /**
     * Définit le mot de passe (hashedPassword doit déjà être hashé)
     * Cette méthode est utilisée par le repository uniquement
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * Retourne le hash du mot de passe pour authentification
     * Retourne null si aucun mot de passe n'est défini
     */
    public String getHashedPasswordForAuthentication() {
        return this.hashedPassword;
    }

    /**
     * Vérifie si un mot de passe est défini en regardant si hashedPassword n'est pas null
     */
    public boolean isPasswordDefined() {
        return this.hashedPassword != null;
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

    public String getEmail() {
        return emailUniv;
    }


}