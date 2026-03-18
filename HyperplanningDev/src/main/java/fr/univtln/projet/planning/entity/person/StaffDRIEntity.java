package fr.univtln.projet.planning.entity.person;

public class StaffDRIEntity extends UserEntity{

    private StaffDRIEntity(String name, String surname) {
        super(name, surname);
    }

    public static StaffDRIEntity StaffDRIFactory(String fname, String lname) {
        StaffDRIEntity s =  UserFactory(fname, lname, StaffDRIEntity::new);
        return s;
    }
}
