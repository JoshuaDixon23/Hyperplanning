package fr.univtln.projet.planning.entity.person;

import java.util.Set;
import java.util.TreeSet;

import fr.univtln.projet.planning.entity.planning.CourseEntity;

public class ProfessorEntity extends UserEntity{
    private final Set<CourseEntity> planning =new TreeSet<>();

    private ProfessorEntity(String name, String surname) {
        super(name, surname);
    }

    public static ProfessorEntity ProfessorFactory(String fname, String lname) {
        ProfessorEntity p =  UserFactory(fname, lname, ProfessorEntity::new);
        return p;
    }
}
