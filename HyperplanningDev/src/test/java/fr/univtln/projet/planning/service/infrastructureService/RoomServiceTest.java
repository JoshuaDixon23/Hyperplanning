package fr.univtln.projet.planning.service.infrastructureService;

import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.modele.infrastructure.RoomType;
import fr.univtln.projet.planning.repository.infrastructureRepository.RoomRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.BuildingRepository;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import fr.univtln.projet.planning.service.academicService.UFRService;
import fr.univtln.projet.planning.service.personService.AdminService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.List;

class RoomServiceTest {

    @Test
    void create_then_findAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        // ------------------ REPOSITORIES ------------------
        BuildingRepository buildingRepo = new BuildingRepository(em);
        RoomRepository roomRepo = new RoomRepository(em);
        UFRRepository ufrRepo = new UFRRepository(em);
        AdminRepository adminRepo = new AdminRepository(em);

        // ------------------ SERVICES ------------------
        CampusService campusService = new CampusService(new fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository(em));
        AdminService adminService = new AdminService(adminRepo);
        UFRService ufrService = new UFRService(ufrRepo, adminService, campusService);
        BuildingService buildingService = new BuildingService(buildingRepo, campusService, ufrService);
        RoomService roomService = new RoomService(roomRepo, buildingService);

        // ------------------ DATA ------------------
        String buildingName = "A";

        // ------------------ CREATE ROOM ------------------
        System.out.println("Creating Room");
        roomService.create(101, 130, RoomType.AMPHITHEATER, buildingName);
        roomService.create(102, 20, RoomType.LAB, buildingName);

        // ------------------ VERIFY ------------------
        List<RoomEntity> rooms = roomService.findAll();
        rooms.forEach(System.out::println);

        em.close();
        emf.close();
    }
}