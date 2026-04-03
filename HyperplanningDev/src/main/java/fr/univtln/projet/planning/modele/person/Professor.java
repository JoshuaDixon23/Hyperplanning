package fr.univtln.projet.planning.modele.person;

import java.util.HashSet;
import java.util.Set;

import fr.univtln.projet.planning.modele.planning.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Professors")
@Getter
@Setter
public class Professor extends User {

    @ManyToMany(mappedBy = "professors")
    private Set<Course> planning = new HashSet<>();

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

    public void addCourse(Course c) {
        if (c != null && !planning.contains(c)) {
            planning.add(c);
            c.getProfessors().add(this); // synchro bidirectionnelle
        }
    }

    public void removeCourse(Course c) {
        if (c != null && planning.contains(c)) {
            planning.remove(c);
            c.getProfessors().remove(this);
        }
    }
}