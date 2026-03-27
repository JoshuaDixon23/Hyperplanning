package fr.univtln.projet.planning.repository.personRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

public class UserRepositoryTest {

    @Test
    void testFindByLastName() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();
        UserRepository ur = new UserRepository(em);
        // verification of connexion to DB
        ur.findByLastName(0, 10, "MICHEl").forEach(System.out::println);
        em.close();
        emf.close();
    }

}
