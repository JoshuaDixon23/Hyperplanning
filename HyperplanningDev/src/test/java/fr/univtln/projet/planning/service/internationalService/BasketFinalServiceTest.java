package fr.univtln.projet.planning.service.internationalService;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.international.BasketFinal;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.internationalRepository.BasketFinalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

public class BasketFinalServiceTest {

    @Test
    void create_addModule_then_getBasket() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        // ------------------ REPO ------------------
        BasketFinalRepository repo = new BasketFinalRepository(em);

        // ------------------ SERVICE ------------------
        BasketFinalService service = new BasketFinalService(repo);

        // ------------------ TRANSACTION ------------------
        em.getTransaction().begin();

        System.out.println("Creating BasketFinal...");
        BasketFinal basket = service.create();

        Module module = new Module();
        module.setName("Maths");

        Group group = new Group();
        group.setNum(1);

        service.addModule(basket.getId(), module, group);

        em.getTransaction().commit();

        // ------------------ VERIFY ------------------
        BasketFinal retrieved = service.getBasket(basket.getId());

        System.out.println("Basket récupéré : " + retrieved);
        System.out.println("Contenu : " + retrieved.getModuleGroup());

        em.close();
        emf.close();
    }
}