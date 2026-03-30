package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.UFREntity;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import fr.univtln.projet.planning.service.infrastructureService.CampusService;
import fr.univtln.projet.planning.service.personService.AdminService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.List;

class UFRServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");

        EntityManager em = emf.createEntityManager();

        // ------------------ REPOSITORIES ------------------
        UFRRepository ufrRepo = new UFRRepository(em);
        CampusRepository campusRepo = new CampusRepository(em);
        AdminRepository adminRepo = new AdminRepository(em);

        // ------------------ SERVICES ------------------
        CampusService campusService = new CampusService(campusRepo);
        AdminService adminService = new AdminService(adminRepo);
        UFRService ufrService = new UFRService(ufrRepo, adminService, campusService);

        System.out.println("Searching Campus");
        System.out.println(campusService.findByCity("La Garde"));

        System.out.println("Searching Admin");
        System.out.println(adminService.findByEmailUniv("jean.admin9@univ-tln.fr"));

        String ufrName = "Lettres";

        // ------------------ TRANSACTION ------------------

        System.out.println("Creating UFR");
        // Admin and Campus below have to already exist in the DB
        ufrService.create(ufrName, "La Garde", "jean.admin9@univ-tln.fr");


        // ------------------ VERIFY ------------------
        List<UFREntity> ufrs = ufrService.findAll();
        ufrs.forEach(System.out::println);

        em.close();
        emf.close();
    }
}