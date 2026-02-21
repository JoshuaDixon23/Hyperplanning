package fr.univtln.projet.planning.entity.person;

public class Admin extends User{
    private Admin(String name, String surname) {
        super(name, surname);
    }

    public static Admin AdminFactory(String fname, String lname) {
        Admin a =  UserFactory(fname, lname, Admin::new);
        return a;
    }
}
