package fr.univtln.projet.planning.entity.person;

import java.util.Set;
import java.util.TreeSet;

import fr.univtln.projet.planning.entity.planning.Course;

public class Professor extends User{
    private final Set<Course> planning =new TreeSet<>();

    private Professor(String name, String surname) {
        super(name, surname);
    }

    public static Professor ProfessorFactory(String fname, String lname) {
        Professor p =  UserFactory(fname, lname, Professor::new);
        return p;
    }

    public String getName() {
        return super.getFirstName()+ " " + super.getLastName();
    }
}
