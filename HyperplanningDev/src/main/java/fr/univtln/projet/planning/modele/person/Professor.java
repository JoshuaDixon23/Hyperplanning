package fr.univtln.projet.planning.modele.person;

import fr.univtln.projet.planning.modele.planning.Course; 
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "Professor")
@Getter
@Setter
public class Professor extends User {

    // @Transient indique à JPA de ne pas créer de colonne ou de table pour cet attribut
    @Transient
    private Set<Course> planning = new TreeSet<>();

    protected Professor() {
        super();
    }

    public Professor(String firstName, String lastName, String emailUniv) {
        super(firstName, lastName, emailUniv);
    }

    /*
    public static Professor ProfessorFactory(String fname, String lname) {
        return UserFactory(fname, lname, Professor::new);
    }

     */
}