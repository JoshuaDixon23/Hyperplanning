package fr.univtln.projet.planning.entity.person;

public class StaffDRI extends User{

    private StaffDRI(String name, String surname) {
        super(name, surname);
    }

    public static StaffDRI StaffDRIFactory(String fname, String lname) {
        StaffDRI s =  UserFactory(fname, lname, StaffDRI::new);
        return s;
    }
}
