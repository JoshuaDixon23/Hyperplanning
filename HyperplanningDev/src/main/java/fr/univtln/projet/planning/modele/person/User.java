package fr.univtln.projet.planning.modele.person;

import fr.univtln.projet.planning.entity.TextTransformation; 
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.function.BiFunction;

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

    @Transient 
    protected static final EmailCreate functionUnivMail = new EmailCreate();

    protected User() {
    }

    protected User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected static <T extends User> T UserFactory(String fname, String lname, BiFunction<String, String, T> constructor) {
        String formattedFname = TextTransformation.capitalize(fname);
        String formattedLname = lname.toUpperCase();

        T user = constructor.apply(formattedFname, formattedLname);
        user.setEmailUniv(functionUnivMail.apply(user));

        return user;
    }

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