package fr.univtln.projet.planning.entity.person;

public class AdminEntity extends UserEntity {
    private AdminEntity(String name, String surname) {
        super(name, surname);
    }

    public static AdminEntity AdminFactory(String fname, String lname) {
        AdminEntity a =  UserFactory(fname, lname, AdminEntity::new);
        return a;
    }
}
