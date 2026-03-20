package fr.univtln.projet.planning.repository.personRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

public class LocalStudentRepositoryTest {

    @Test
    void testFindByPromo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();
        LocalStudentRepository lsr = new LocalStudentRepository(em);
        // verification of connexion to DB
        //lsr.findAll(0, 10).forEach(System.out::println);
        lsr.findByPromo(0, 10, 1).forEach(System.out::println);
        em.close();
        emf.close();
    }
}
