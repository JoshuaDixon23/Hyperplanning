package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.StaffDRIEntity;
import fr.univtln.projet.planning.repository.personRepository.StaffDRIRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.List;

class StaffDRIServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");

        String firstName = "Alice";
        String lastName = "DRI";

        EntityManager em = emf.createEntityManager();

        StaffDRIRepository repo = new StaffDRIRepository(em);
        StaffDRIService service = new StaffDRIService(repo);

        System.out.println("Creating StaffDRI");
        service.create(firstName, lastName);

        List<StaffDRIEntity> staff = service.findAll(0, 10);
        staff.forEach(System.out::println);

        em.close();
        emf.close();
    }
}