package fr.univtln.projet.planning.mapper.infrastracture;

import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;
import fr.univtln.projet.planning.modele.infrastructure.Campus;

public class CampusMapper {

    private CampusMapper(){
        // Prevent instantiation of utility class
    }

    public static CampusEntity toDomain(Campus c) {
        if (c == null) return null;
        return CampusEntity.CampusFactory(c.getCity(), c.getImageFileName());
    }

    public static Campus toJpa(CampusEntity c) {
        if (c == null) return null;
        return Campus.CampusFactory(c.getCity(), c.getImageFileName());
    }
}
