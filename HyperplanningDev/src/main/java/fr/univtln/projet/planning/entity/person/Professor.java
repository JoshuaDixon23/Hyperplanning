package fr.univtln.projet.planning.entity.person;

public class Professor extends User{

    private Professor(String name, String surname) {
        super(name, surname);
    }

    public static Professor ProfessorFactory(String fname, String lname) {
        Professor p =  UserFactory(fname, lname, Professor::new);
        return p;
    }
}
