package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import fr.univtln.projet.planning.repository.personRepository.LocalStudentRepository;
import fr.univtln.projet.planning.repository.personRepository.ProfessorRepository;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.service.academicService.GroupService;
import fr.univtln.projet.planning.service.academicService.PromoService;
import fr.univtln.projet.planning.service.academicService.UFRService;
import fr.univtln.projet.planning.service.infrastructureService.CampusService;
import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.service.personService.AdminService;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.List;

class LocalStudentServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        // ------------------ REPOS ------------------
        LocalStudentRepository studentRepo = new LocalStudentRepository(em);
        PromoRepository promoRepo = new PromoRepository(em);
        UFRRepository ufrRepo = new UFRRepository(em);
        AdminRepository adminRepo = new AdminRepository(em);
        CampusRepository campusRepo = new CampusRepository(em);
        GroupRepository groupRepository = new GroupRepository(em);
        ModuleRepository moduleRepository = new ModuleRepository(em);
        ProfessorRepository professorRepository = new ProfessorRepository(em);

        // ------------------ SERVICES ------------------
        CampusService campusService = new CampusService(campusRepo);
        AdminService adminService = new AdminService(adminRepo);
        UFRService ufrService = new UFRService(ufrRepo, adminService, campusService);
        PromoService promoService = new PromoService(promoRepo, ufrService);
        ProfessorService professorService = new ProfessorService(professorRepository);
        ModuleService moduleService = new ModuleService(moduleRepository, professorService);
        GroupService groupService = new GroupService(groupRepository, promoService, moduleService); // repository non nécessaire pour test minimal
        LocalStudentService studentService = new LocalStudentService(studentRepo, promoService, groupService);

        // ------------------ CREATE STUDENT ------------------
        System.out.println("Creating LocalStudent");
        LocalStudentEntity student = studentService.createWithPromo("Nikita", "Rodyhin",
                "rodygin.nikita2005@gmail.com", "LLCER", 2026, StudyLevel.L2);

        // ------------------ VERIFY ------------------
        List<LocalStudentEntity> students = studentService.findAll(0, 10);
        students.forEach(System.out::println);

        em.close();
        emf.close();
    }
}