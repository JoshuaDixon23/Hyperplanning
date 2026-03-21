package fr.univtln.projet.planning.service.infrastructureService;

import fr.univtln.projet.planning.entity.infrastructure.BuildingEntity;
import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.modele.infrastructure.Campus;
import fr.univtln.projet.planning.modele.infrastructure.Day;
import fr.univtln.projet.planning.repository.infrastructureRepository.BuildingRepository;
import fr.univtln.projet.planning.service.academicService.UFRService;

import java.util.List;
import java.util.Map;

public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final CampusService campusService;
    private final UFRService ufrService;

    public BuildingService(BuildingRepository buildingRepository,
                           CampusService campusService,
                           UFRService ufrService) {
        this.buildingRepository = buildingRepository;
        this.campusService = campusService;
        this.ufrService = ufrService;
    }

    // ------------------ MAPPERS ------------------

    BuildingEntity toDomain(Building jpaBuilding) {
        if (jpaBuilding == null) return null;

        BuildingEntity entity = BuildingEntity.BuildingFactory(
                jpaBuilding.getName(),
                jpaBuilding.getLocalisation(),
                jpaBuilding.getOpeningHours()
        );

        // mapper Campus
        if (jpaBuilding.getCampus() != null) {
            entity.setCampus(campusService.toDomain(jpaBuilding.getCampus()));
        }

        // mapper UFR
        if (jpaBuilding.getUfr() != null) {
            entity.setUfr(ufrService.toDomain(jpaBuilding.getUfr()));
        }

        return entity;
    }

    public Building toJpa(BuildingEntity b) {
        if (b == null) return null;
        // resolve the problem of OpeningHours time (make this class public maybe and not internal for Building)
        return Building.BuildingFactory(b.getName(), b.getLocalisation(), null /* b.getOpeningHours()*/);
    }

    // ------------------ CREATE ------------------

    public BuildingEntity create(BuildingEntity entity) {
        Building saved = buildingRepository.save(toJpa(entity));
        return toDomain(saved);
    }

    public BuildingEntity create(String name, String localisation, Map<Day, Building.Hours>  openingHours,
                                 String ufrName, String campusCity) {
        UFR ufr = ufrService.findJpaByName(ufrName);

        Campus campus = campusService.findJpaByCity(campusCity);
        if (campus == null) {
            throw new IllegalArgumentException("Campus not found: " + campusCity);
        }

        Building entity = Building.BuildingFactory(name, localisation, openingHours);
        entity.setUfr(ufr);
        entity.setCampus(campus);

        Building saved = buildingRepository.save(entity);
        return toDomain(saved);
    }

    private UFR getUfr(String ufrName) {
        UFR ufr = ufrService.findJpaByName(ufrName);
        if (ufr == null) {
            throw new IllegalArgumentException("UFR not found: " + ufrName);
        }
        return ufr;
    }

    // ------------------ FIND ------------------

    public BuildingEntity findById(Long id) {
        return buildingRepository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    public List<BuildingEntity> findAll() {
        return buildingRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }
    
    public BuildingEntity findByName(String name) {
        return buildingRepository.findByName(name)
                .map(this::toDomain)
                .orElse(null);
    }

    public Building findJpaByName(String name) {
        return buildingRepository.findByName(name)
                .orElse(null);
    }
}