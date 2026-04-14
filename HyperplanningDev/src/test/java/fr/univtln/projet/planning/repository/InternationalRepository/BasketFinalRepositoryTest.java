package fr.univtln.projet.planning.repository.InternationalRepository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.international.BasketFinal;
import fr.univtln.projet.planning.repository.internationalRepository.BasketFinalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class BasketFinalRepositoryTest {

    @Test
    void testBasketFinalRepository() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        BasketFinalRepository repo = new BasketFinalRepository(em);

        Optional<BasketFinal> basketOpt = repo.findWithAll(1L);
        basketOpt.ifPresent(b -> {
            System.out.println("Basket trouvé : " + b);
        });

        ModuleEntity module = ModuleEntity.builder()
                .name("Maths")
                .build();
        List<BasketFinal> baskets = repo.findByModule(module);
        baskets.forEach(System.out::println);

        boolean exists = repo.containsModule(1L, module);
        System.out.println("Contient module ? " + exists);

        em.close();
        emf.close();
    }
}