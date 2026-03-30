package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.modele.academic.GroupType;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GroupServiceTest {

    @Test
    void createGroup_and_findAll() {
        // Création EM / Factory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        // ------------------ REPOSITORIES ------------------
        AdminRepository adminRepo = new AdminRepository(em);
        CampusRepository campusRepo = new CampusRepository(em);
        UFRRepository ufrRepo = new UFRRepository(em);
        PromoRepository promoRepo = new PromoRepository(em);
        GroupRepository groupRepo = new GroupRepository(em);

        // ------------------ SERVICES ------------------
        AdminService adminService = new AdminService(adminRepo);
        CampusService campusService = new CampusService(campusRepo);
        UFRService ufrService = new UFRService(ufrRepo, adminService, campusService);
        PromoService promoService = new PromoService(promoRepo, ufrService);
        GroupService groupService = new GroupService(groupRepo, promoService, null); // ModuleService null pour test

        // Création du groupe
        GroupEntity group = groupService.create(1, GroupType.TD, "LLCER", StudyLevel.L2, 2026);

        // Vérification simple
        Set<GroupEntity> uniqueGroups = new HashSet<>(groupService.findAll());
        uniqueGroups.forEach(System.out::println);

        em.close();
        emf.close();
    }
}