package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
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

class PromoServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        // ------------------ REPOS ------------------
        PromoRepository promoRepo = new PromoRepository(em);
        UFRRepository ufrRepo = new UFRRepository(em);
        AdminRepository adminRepo = new AdminRepository(em);
        CampusRepository campusRepo = new CampusRepository(em);

        // ------------------ SERVICES ------------------
        CampusService campusService = new CampusService(campusRepo);
        AdminService adminService = new AdminService(adminRepo);
        UFRService ufrService = new UFRService(ufrRepo, adminService, campusService);
        PromoService promoService = new PromoService(promoRepo, ufrService);

        // ------------------ CREATE PROMO ------------------

        System.out.println("Creating Promo");
        PromoEntity promo = promoService.create(
                "LLCER", 2026, StudyLevel.L2, "Lettres"
        );

        // ------------------ VERIFY ------------------
        List<PromoEntity> promos = promoService.findAll();
        promos.forEach(System.out::println);

        em.close();
        emf.close();
    }
}