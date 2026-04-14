package fr.univtln.projet.planning.repository.InternationalRepository;

import fr.univtln.projet.planning.modele.international.BasketModule;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.repository.internationalRepository.BasketModuleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class BasketModuleRepositoryTest {

    @Test
    void testBasketModuleRepository() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        BasketModuleRepository repo = new BasketModuleRepository(em);

        // create student
        InternationalStudentEntity student = new InternationalStudentEntity("John", "Doe");
        student.setEmailUniv("john@univ.fr");

        // Test findByStudent
        Optional<BasketModule> basketByStudent = repo.findByStudent(student);
        basketByStudent.ifPresent(b -> {
            System.out.println("Basket trouvé pour étudiant : " + b);
        });

        //  Test findWithModules
        Optional<BasketModule> basketWithModules = repo.findWithModules(1L);
        basketWithModules.ifPresent(b -> {
            System.out.println("Basket avec modules : " + b);
            System.out.println("Modules : " + b.getModules());
        });

        em.close();
        emf.close();
    }
}