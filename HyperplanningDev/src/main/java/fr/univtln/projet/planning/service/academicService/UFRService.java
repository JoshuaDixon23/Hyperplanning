package fr.univtln.projet.planning.service.academicService;

import java.util.List;
import java.util.stream.Collectors;             // Modèle JPA

import fr.univtln.projet.planning.entity.academic.UFREntity;       // Modèle Métier (DTO)
import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;

public class UFRService {

    private final UFRRepository ufrRepository;

    public UFRService(UFRRepository ufrRepository) {
        this.ufrRepository = ufrRepository;
    }

    // --- MÉTHODES MÉTIER ---

    public UFREntity createUFR(UFREntity domainUfr) {
        UFR jpaUfr = toJpaModel(domainUfr);
        UFR savedUfr = ufrRepository.save(jpaUfr);
        return toDomainEntity(savedUfr);
    }

    public UFREntity getUFRById(Long id) {
        return ufrRepository.findById(id)
                .map(this::toDomainEntity)
                .orElse(null);
    }

    public List<UFREntity> getAllUFRs() {
        return ufrRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    // --- MÉTHODES DE MAPPING (Traduction) ---

    /**
     * Convertit le modèle JPA (Base de données) vers l'objet Métier (UFREntity)
     */
    private UFREntity toDomainEntity(UFR jpaUfr) {
        if (jpaUfr == null) return null;

        // TODO: Gérer la conversion de jpaUfr.getCampus() vers CampusEntity
        // TODO: Gérer la conversion de jpaUfr.getAdmin() vers AdminEntity

        UFREntity domainUfr = UFREntity.UFRFactory(
            jpaUfr.getName(), 
            null, // Placeholder pour le CampusEntity
            null  // Placeholder pour l'AdminEntity
        );

        // TODO: Boucler sur jpaUfr.getPromos() pour les ajouter à domainUfr
        // TODO: Boucler sur jpaUfr.getBuildings() pour les ajouter à domainUfr

        return domainUfr;
    }

    /**
     * Convertit l'objet Métier (UFREntity) vers le modèle JPA (Base de données)
     */
    private UFR toJpaModel(UFREntity domainUfr) {
        if (domainUfr == null) return null;

        // TODO: Gérer la conversion de domainUfr.getCampus() vers Campus JPA
        // TODO: Gérer la conversion de domainUfr.getAdmin() vers Admin JPA

        UFR jpaUfr = UFR.UFRFactory(
            domainUfr.getName(), 
            null, // Placeholder pour le Campus JPA
            null  // Placeholder pour l'Admin JPA
        );

        // TODO: Boucler sur domainUfr.getModules() (ou getPromos()) pour les ajouter à jpaUfr
        // TODO: Boucler sur domainUfr.getBuildings() pour les ajouter à jpaUfr

        return jpaUfr;
    }
}