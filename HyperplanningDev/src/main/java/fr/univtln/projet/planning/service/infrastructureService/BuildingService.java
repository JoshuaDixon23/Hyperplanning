package fr.univtln.projet.planning.service.infrastructureService;

import fr.univtln.projet.planning.entity.infrastructure.BuildingEntity;
import fr.univtln.projet.planning.mapper.infrastracture.BuildingMapper;
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

    // ------------------ CREATE ------------------

    public BuildingEntity create(BuildingEntity entity) {
        Building saved = buildingRepository.save(BuildingMapper.toJpa(entity));
        return BuildingMapper.toDomain(saved);
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
        return BuildingMapper.toDomain(saved);
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
                .map(BuildingMapper::toDomain)
                .orElse(null);
    }

    public List<BuildingEntity> findAll() {
        return buildingRepository.findAll()
                .stream()
                .map(BuildingMapper::toDomain)
                .toList();
    }
    
    public BuildingEntity findByName(String name) {
        return buildingRepository.findByName(name)
                .map(BuildingMapper::toDomain)
                .orElse(null);
    }

    public Building findJpaByName(String name) {
        return buildingRepository.findByName(name)
                .orElse(null);
    }
}