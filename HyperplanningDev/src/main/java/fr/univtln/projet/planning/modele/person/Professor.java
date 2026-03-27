package fr.univtln.projet.planning.modele.person;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.planning.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Professor")
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