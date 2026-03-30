package fr.univtln.projet.planning.mapper.person;

import fr.univtln.projet.planning.entity.person.StaffDRIEntity;
import fr.univtln.projet.planning.modele.person.StaffDRI;

public class StaffDRIMapper {

    private StaffDRIMapper() {
        // Prevent instantiation of utility class
    }

    public static StaffDRIEntity toDomain(StaffDRI s){
        if (s == null) return null;
        return new StaffDRIEntity(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv()
        );
    }

    public static StaffDRI toJpa(StaffDRIEntity s){
        if (s == null) return null;
        return new StaffDRI(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv()
        );
    }
}
