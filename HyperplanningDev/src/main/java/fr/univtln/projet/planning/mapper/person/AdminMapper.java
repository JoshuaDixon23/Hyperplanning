package fr.univtln.projet.planning.mapper.person;

import fr.univtln.projet.planning.entity.person.AdminEntity;
import fr.univtln.projet.planning.modele.person.Admin;

public class AdminMapper {

    private AdminMapper() {
        // Prevent instantiation of utility class
    }

    public static AdminEntity toDomain(Admin a){
        if (a == null) return null;
        return new AdminEntity(
                a.getFirstName(),
                a.getLastName(),
                a.getEmailUniv()
        );
    }

    public static Admin toJpa(AdminEntity a){
        if (a == null) return null;
        return new Admin(
                a.getFirstName(),
                a.getLastName(),
                a.getEmailUniv()
        );
    }
}
