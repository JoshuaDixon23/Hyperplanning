package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.repository.personRepository.InternationalStudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

public class InternationalStudentServiceTest {
    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe2@gmail.com";

        EntityManager em = emf.createEntityManager();

        InternationalStudentRepository repo = new InternationalStudentRepository(em);

        InternationalStudentService service = new InternationalStudentService(repo);

        System.out.println("Creating LocalStudent Service");
        service.create(firstName, lastName, email);

        List<InternationalStudentEntity> students = service.findAll(0, 10);
        students.forEach(System.out::println);

        em.close();
        emf.close();
    }
}
