package fr.univtln.projet.planning.mapper.infrastracture;

import fr.univtln.projet.planning.entity.infrastructure.BuildingEntity;
import fr.univtln.projet.planning.mapper.academic.UFRMapper;
import fr.univtln.projet.planning.modele.infrastructure.Building;

public class BuildingMapper {

    private BuildingMapper() {
        // Prevent instantiation of utility class
    }

    public static BuildingEntity toDomain(Building jpaBuilding) {
        if (jpaBuilding == null) return null;

        BuildingEntity entity = BuildingEntity.BuildingFactory(
                jpaBuilding.getName(),
                jpaBuilding.getLocalisation(),
                jpaBuilding.getOpeningHours()
        );

        // mapper Campus
        if (jpaBuilding.getCampus() != null) {
            entity.setCampus(CampusMapper.toDomain(jpaBuilding.getCampus()));
        }

        // mapper UFR
        if (jpaBuilding.getUfr() != null) {
            entity.setUfr(UFRMapper.toDomain(jpaBuilding.getUfr()));
        }

        return entity;
    }

    public static Building toJpa(BuildingEntity b) {
        if (b == null) return null;
        // resolve the problem of OpeningHours time (make this class public maybe and not internal for Building)
        return Building.BuildingFactory(b.getName(), b.getLocalisation(), null /* b.getOpeningHours()*/);
    }

}
