package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.modele.planning.Language;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.repository.personRepository.ProfessorRepository;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class ModuleServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        // ------------------ REPOS ------------------
        ProfessorRepository profRepo = new ProfessorRepository(em);
        ModuleRepository modRepo = new ModuleRepository(em);

        // ------------------ SERVICES ------------------
        ProfessorService professorService = new ProfessorService(profRepo);
        ModuleService moduleService = new ModuleService(modRepo, professorService);

        // ------------------ CREATE MODULE ------------------
        System.out.println("Creating Module");
        moduleService.create(
                "PHY101",
                "Physique I",
                Language.FRENCH,
                6.0f,
                "alan.turing8@univ-tln.fr",
                Map.of() // pas utilisé ici
        );

        // ------------------ VERIFY ------------------
        List<?> modules = moduleService.findAll();
        modules.forEach(System.out::println);

        em.close();
        emf.close();
    }
}