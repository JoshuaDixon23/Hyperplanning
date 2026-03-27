package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.AdminEntity;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.List;

class AdminServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");

        String firstName = "Jean";
        String lastName = "Admin";

        EntityManager em = emf.createEntityManager();

        AdminRepository repo = new AdminRepository(em);
        AdminService service = new AdminService(repo);

        System.out.println("Creating Admin");
        service.create(firstName, lastName);

        List<AdminEntity> admins = service.findAll(0, 10);
        admins.forEach(System.out::println);

        em.close();
        emf.close();
    }
}