package fr.univtln.projet.planning.service.internationalService;

import fr.univtln.projet.planning.modele.international.BasketModule;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.modele.person.InternationalStudent;
import fr.univtln.projet.planning.repository.internationalRepository.BasketModuleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

public class BasketModuleServiceTest {

    @Test
    void create_then_getBasket() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        BasketModuleRepository repo = new BasketModuleRepository(em);
        BasketModuleService service = new BasketModuleService(repo);
        em.getTransaction().begin();

        // create student
        InternationalStudent student = new InternationalStudent("John", "Doe","jdoe550@univ-tln.fr","johndoe@gmail.com");
        student.setEmailUniv("john@univ.fr");

        em.persist(student);

        // create basket
        System.out.println("Creating BasketModule...");
        BasketModule basket = service.create(student);

        em.getTransaction().commit();

        InternationalStudentEntity studentEntity = em.find(
                InternationalStudentEntity.class,
                student.getId()
        );

        BasketModule retrieved = service.getBasket(studentEntity);

        System.out.println("Basket récupéré : " + retrieved);

        em.close();
        emf.close();
    }
}