package fr.univtln.projet.planning.service.infrastructureService;

import java.util.List;
import java.util.stream.Collectors;             // Modèle JPA (BDD)

import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;       // Modèle Métier (DTO)
import fr.univtln.projet.planning.modele.infrastructure.Campus;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import fr.univtln.projet.planning.service.academicService.UFRService;

public class CampusService {

    /*
    private final CampusRepository campusRepository;
    private final BuildingService buildingService;
    private final UFRService ufrService;

        /// Constructeur
    public CampusService(CampusRepository campusRepository, BuildingService buildingService, UFRService ufrService) {
        this.campusRepository = campusRepository;
        this.buildingService = buildingService;
        this.ufrService = ufrService;
    }
    // --- MÉTHODES MÉTIER ---

    public CampusEntity createCampus(CampusEntity domainCampus) {
        Campus jpaCampus = toJpaModel(domainCampus);
        Campus savedCampus = campusRepository.save(jpaCampus);
        return toDomainEntity(savedCampus);
    }

    public CampusEntity getCampusById(Long id) {
        return campusRepository.findById(id)
                .map(this::toDomainEntity)
                .orElse(null);
    }

    public List<CampusEntity> getAllCampuses() {
        return campusRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    // --- MAPPINGS ---
    private CampusEntity toDomainEntity(Campus jpaCampus) {
        if (jpaCampus == null) return null;
        CampusEntity domainCampus = CampusEntity.CampusFactory(jpaCampus.getCity(), jpaCampus.getImageFileName());

        if (jpaCampus.getBuildings() != null) {
            jpaCampus.getBuildings().forEach(b -> domainCampus.addBuilding(buildingService.toDomainEntity(b)));
        }
        if (jpaCampus.getUfrs() != null) {
            jpaCampus.getUfrs().forEach(u -> domainCampus.addUfr(ufrService.toDomainEntity(u)));
        }
        return domainCampus;
    }

    private Campus toJpaModel(CampusEntity domainCampus) {
        if (domainCampus == null) return null;
        Campus jpaCampus = Campus.CampusFactory(domainCampus.getCity(), domainCampus.getImageFileName());

        if (domainCampus.getBuildings() != null) {
            domainCampus.getBuildings().forEach(b -> jpaCampus.addBuilding(buildingService.toJpaModel(b)));
        }
        if (domainCampus.getUfrs() != null) {
            domainCampus.getUfrs().forEach(u -> jpaCampus.addUfr(ufrService.toJpaModel(u)));
        }
        return jpaCampus;
    }

     */
}