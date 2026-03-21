package fr.univtln.projet.planning.service.infrastructureService;

import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.List;

class CampusServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");

        String city = "Toulon";
        String image = "toulon.png";

        EntityManager em = emf.createEntityManager();

        CampusRepository repo = new CampusRepository(em);
        CampusService service = new CampusService(repo);

        System.out.println("Creating Campus");
        service.create(city, image);

        List<CampusEntity> campuses = service.findAll();
        campuses.forEach(System.out::println);

        em.close();
        emf.close();
    }
}