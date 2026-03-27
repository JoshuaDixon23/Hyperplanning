package fr.univtln.projet.planning.service.infrastructureService;

import fr.univtln.projet.planning.entity.infrastructure.BuildingEntity;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.BuildingRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import fr.univtln.projet.planning.service.academicService.UFRService;
import fr.univtln.projet.planning.service.personService.AdminService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BuildingServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        // ------------------ REPOS ------------------
        BuildingRepository buildingRepo = new BuildingRepository(em);
        CampusRepository campusRepo = new CampusRepository(em);
        UFRRepository ufrRepo = new UFRRepository(em);
        AdminRepository adminRepo = new AdminRepository(em);

        // ------------------ SERVICES ------------------
        CampusService campusService = new CampusService(campusRepo);
        AdminService adminService = new AdminService(adminRepo);
        UFRService ufrService = new UFRService(ufrRepo, adminService, campusService);
        BuildingService buildingService = new BuildingService(buildingRepo, campusService, ufrService);

        // ------------------ DATA ------------------
        String city = "Toulon";
        String buildingName = "A";
        String localisation = "Center";

        // ⚠️ hours are given empty, change of visibility of Hours may be needed
        Map<fr.univtln.projet.planning.modele.infrastructure.Day, Building.Hours> hours = new HashMap<>();

        // ------------------ TRANSACTION ------------------
        System.out.println("Creating Building");
        buildingService.create(buildingName, localisation, hours, "Lettres", city);


        // ------------------ VERIFY ------------------
        List<BuildingEntity> buildings = buildingService.findAll();
        buildings.forEach(System.out::println);

        em.close();
        emf.close();
    }
}