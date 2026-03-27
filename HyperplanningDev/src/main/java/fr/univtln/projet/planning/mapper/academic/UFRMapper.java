package fr.univtln.projet.planning.mapper.academic;

import fr.univtln.projet.planning.entity.academic.UFREntity;
import fr.univtln.projet.planning.mapper.infrastracture.CampusMapper;
import fr.univtln.projet.planning.mapper.person.AdminMapper;
import fr.univtln.projet.planning.modele.academic.UFR;

public class UFRMapper {

    private UFRMapper() {
        // Prevent instantiation of utility class
    }

    public static UFREntity toDomain(UFR u) {
        if (u == null) return null;
        return UFREntity.UFRFactory(u.getName(), CampusMapper.toDomain(u.getCampus()),
                AdminMapper.toDomain(u.getAdmin()));
    }

    public static UFR toJpa(UFREntity u) {
        if (u == null) return null;
        return UFR.UFRFactory(u.getName(), CampusMapper.toJpa(u.getCampus()),
                AdminMapper.toJpa(u.getAdmin()));
    }
}
