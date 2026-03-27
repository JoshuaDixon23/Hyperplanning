package fr.univtln.projet.planning.entity.person;

import java.util.Set;
import java.util.HashSet;

import fr.univtln.projet.planning.entity.planning.CourseEntity;

public class ProfessorEntity extends UserEntity {
    private final Set<CourseEntity> planning = new HashSet<>();

    private ProfessorEntity(String name, String surname) {
        super(name, surname);
    }

    public static ProfessorEntity ProfessorFactory(String fname, String lname) {
        ProfessorEntity p = UserFactory(fname, lname, ProfessorEntity::new);
        return p;
    }

    public String getName() {
        return super.getFirstName();
    }

    public String getSurname() {
        return super.getLastName();
    }

    public void addCourse(CourseEntity c) {
        if (c != null && !planning.contains(c)) {
            planning.add(c);
            c.getProfessors().add(this); // synchro bidirectionnelle
        }
    }

    public void removeCourse(CourseEntity c) {
        if (c != null && planning.contains(c)) {
            planning.remove(c);
            c.getProfessors().remove(this);
        }
    }
}
