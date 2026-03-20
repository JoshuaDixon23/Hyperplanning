package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.repository.personRepository.LocalStudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

class LocalStudentServiceTest {

    @Test
    void create_then_findAll_should_work() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@gmail.com";

        EntityManager em = emf.createEntityManager();

        LocalStudentRepository repo = new LocalStudentRepository(em);

        LocalStudentService service = new LocalStudentService(repo, null);

        System.out.println("Creating LocalStudent Service");
        service.create(firstName, lastName, email);

        List<LocalStudentEntity> students = service.findAll(0, 10);
        students.forEach(System.out::println);

        em.close();
        emf.close();
    }
}