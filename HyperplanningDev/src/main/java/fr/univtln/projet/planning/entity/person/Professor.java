package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.entity.planning.Course;

import java.util.Set;
import java.util.TreeSet;

public class Professor extends User{
    private final Set<Course> planning =new TreeSet<>();

    private Professor(String name, String surname) {
        super(name, surname);
    }

    public static Professor ProfessorFactory(String fname, String lname) {
        Professor p =  UserFactory(fname, lname, Professor::new);
        return p;
    }
}
