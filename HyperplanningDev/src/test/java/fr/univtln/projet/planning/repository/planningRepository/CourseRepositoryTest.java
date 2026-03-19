package fr.univtln.projet.planning.repository.planningRepository;

import fr.univtln.projet.planning.modele.planning.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class CourseRepositoryTest {


    //test a modifier requette par id et non par group

//    @Test
//    void testFindByLastName() {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
//        EntityManager em = emf.createEntityManager();
//        Class<Course>  entityClass = Course.class;
//
//        CourseRepository cr = new CourseRepository(entityClass,em);
//        // verification of connexion to DB
//        Instant begining = Instant.parse("2026-08-15T00:00:00Z");
//        Instant end = Instant.parse("2026-10-15T00:00:00Z");
//
//        group
//
//        cr.getPlanningByGroup(2, begining , end).forEach(System.out::println);
//        em.close();
//        emf.close();
//    }
}