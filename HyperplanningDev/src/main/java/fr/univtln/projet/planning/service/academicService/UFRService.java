package fr.univtln.projet.planning.service.academicService;

import java.util.List;
import java.util.stream.Collectors;             // Modèle JPA

import fr.univtln.projet.planning.entity.academic.UFREntity;       // Modèle Métier (DTO)
import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.service.infrastructureService.BuildingService;
import fr.univtln.projet.planning.service.infrastructureService.CampusService;
import fr.univtln.projet.planning.service.personService.AdminService;

public class UFRService {

    private final UFRRepository ufrRepository;
    private final CampusService campusService;
    private final AdminService adminService;
    private final BuildingService buildingService;
    private final PromoService promoService;

    public UFRService(UFRRepository ufrRepository, CampusService campusService, AdminService adminService, BuildingService buildingService, PromoService promoService) {
        this.ufrRepository = ufrRepository;
        this.campusService = campusService;
        this.adminService = adminService;
        this.buildingService = buildingService;
        this.promoService = promoService;
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

    // --- MAPPINGS ---
    private UFREntity toDomainEntity(UFR jpaUfr) {
        if (jpaUfr == null) return null;

        UFREntity domainUfr = UFREntity.UFRFactory(
            jpaUfr.getName(), 
            campusService.toDomainEntity(jpaUfr.getCampus()),
            adminService.toDomainEntity(jpaUfr.getAdmin())
        );

        if (jpaUfr.getPromos() != null) {
            jpaUfr.getPromos().forEach(p -> domainUfr.addPromo(promoService.toDomainEntity(p))); 
        }
        if (jpaUfr.getBuildings() != null) {
            jpaUfr.getBuildings().forEach(b -> domainUfr.addBuilding(buildingService.toDomainEntity(b)));
        }
        return domainUfr;
    }

    private UFR toJpaModel(UFREntity domainUfr) {
        if (domainUfr == null) return null;

        UFR jpaUfr = UFR.UFRFactory(
            domainUfr.getName(), 
            campusService.toJpaModel(domainUfr.getCampus()), 
            adminService.toJpaModel(domainUfr.getAdmin())
        );

        if (domainUfr.getModules() != null) { // getModules() correspond à tes promos
            domainUfr.getModules().forEach(p -> jpaUfr.addPromo(promoService.toJpaModel(p)));
        }
        if (domainUfr.getBuildings() != null) {
            domainUfr.getBuildings().forEach(b -> jpaUfr.addBuilding(buildingService.toJpaModel(b)));
        }
        return jpaUfr;
    }
}