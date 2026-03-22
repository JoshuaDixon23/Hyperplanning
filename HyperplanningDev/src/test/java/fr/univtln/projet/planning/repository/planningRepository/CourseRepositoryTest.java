package fr.univtln.projet.planning.repository.planningRepository;

import fr.univtln.projet.planning.modele.planning.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class CourseRepositoryTest {


    //test a modifier requette par id et non par group

    @Test
    void testGetPlanningByGroup() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();
        Class<Course>  entityClass = Course.class;

        CourseRepository cr = new CourseRepository(em);
        // verification of connexion to DB
        Instant beginning = Instant.parse("2026-09-14T00:00:00Z");
        Instant end = Instant.parse("2026-09-16T00:00:00Z");

        ZoneId zone = ZoneId.of("Europe/Paris");

        LocalDate beginningDate = beginning.atZone(zone).toLocalDate();
        LocalDate endDate = end.atZone(zone).toLocalDate();

        cr.getPlanningByGroup(2L, beginningDate , endDate).forEach(System.out::println);
        em.close();
        emf.close();
    }

    /*
    @Test
    void testGetPlanningByModule() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();
        Class<Course>  entityClass = Course.class;

        CourseRepository cr = new CourseRepository(em);
        // verification of connexion to DB
        Instant beginning = Instant.parse("2026-09-14T00:00:00Z");
        Instant end = Instant.parse("2026-09-16T00:00:00Z");

        ZoneId zone = ZoneId.of("Europe/Paris");

        LocalDate beginningDate = beginning.atZone(zone).toLocalDate();
        LocalDate endDate = end.atZone(zone).toLocalDate();

        cr.getPlanningByModule("M-JAVA-01", beginningDate , endDate).forEach(System.out::println);
        em.close();
        emf.close();
    }


    @Test
    void testGetPlanningByRoom() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();
        Class<Course>  entityClass = Course.class;

        CourseRepository cr = new CourseRepository(em);
        // verification of connexion to DB
        Instant beginning = Instant.parse("2026-09-14T00:00:00Z");
        Instant end = Instant.parse("2026-09-16T00:00:00Z");

        ZoneId zone = ZoneId.of("Europe/Paris");

        LocalDate beginningDate = beginning.atZone(zone).toLocalDate();
        LocalDate endDate = end.atZone(zone).toLocalDate();

        cr.getPlanningByRoom(1L, beginningDate , endDate).forEach(System.out::println);
        em.close();
        emf.close();
    }

     */
}