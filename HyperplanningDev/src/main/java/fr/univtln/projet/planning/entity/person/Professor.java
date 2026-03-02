package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.entity.planning.Planning;

public class Professor extends User{
    private Planning planning;

    private Professor(String name, String surname) {
        super(name, surname);
        planning = new Planning();
    }

    public static Professor ProfessorFactory(String fname, String lname) {
        Professor p =  UserFactory(fname, lname, Professor::new);
        return p;
    }
}
