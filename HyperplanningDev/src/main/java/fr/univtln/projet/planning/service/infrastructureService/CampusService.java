package fr.univtln.projet.planning.service.infrastructureService;

import java.util.List;
import java.util.stream.Collectors;             // Modèle JPA (BDD)

import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;       // Modèle Métier (DTO)
import fr.univtln.projet.planning.modele.infrastructure.Campus;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;

public class CampusService {

    private final CampusRepository campusRepository;

    public CampusService(CampusRepository campusRepository) {
        this.campusRepository = campusRepository;
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

    // --- MÉTHODES DE MAPPING (Traduction) ---

    /**
     * Convertit le modèle JPA (Base de données) vers l'objet Métier (CampusEntity)
     */
    private CampusEntity toDomainEntity(Campus jpaCampus) {
        if (jpaCampus == null) return null;

        CampusEntity domainCampus = CampusEntity.CampusFactory(
            jpaCampus.getCity(), 
            jpaCampus.getImageFileName()
        );

        // TODO: Boucler sur jpaCampus.getBuildings() pour les convertir et les ajouter à domainCampus
        // TODO: Boucler sur jpaCampus.getUfrs() pour les convertir et les ajouter à domainCampus

        return domainCampus;
    }

    /**
     * Convertit l'objet Métier (CampusEntity) vers le modèle JPA (Base de données)
     */
    private Campus toJpaModel(CampusEntity domainCampus) {
        if (domainCampus == null) return null;

        Campus jpaCampus = Campus.CampusFactory(
            domainCampus.getCity(), 
            domainCampus.getImageFileName()
        );

        // TODO: Boucler sur domainCampus.getBuildings() pour les convertir et les ajouter à jpaCampus
        // TODO: Boucler sur domainCampus.getUfrs() (quand le getter sera créé) pour les convertir et les ajouter à jpaCampus

        return jpaCampus;
    }
}