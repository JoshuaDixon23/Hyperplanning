package fr.univtln.projet.planning.service;

import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.BuildingRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.RoomRepository;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import fr.univtln.projet.planning.repository.personRepository.InternationalStudentRepository;
import fr.univtln.projet.planning.repository.personRepository.LocalStudentRepository;
import fr.univtln.projet.planning.repository.personRepository.ProfessorRepository;
import fr.univtln.projet.planning.repository.personRepository.StaffDRIRepository;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.service.academicService.GroupService;
import fr.univtln.projet.planning.service.academicService.PromoService;
import fr.univtln.projet.planning.service.academicService.UFRService;
import fr.univtln.projet.planning.service.infrastructureService.BuildingService;
import fr.univtln.projet.planning.service.infrastructureService.CampusService;
import fr.univtln.projet.planning.service.infrastructureService.RoomService;
import fr.univtln.projet.planning.service.personService.AdminService;
import fr.univtln.projet.planning.service.personService.InternationalStudentService;
import fr.univtln.projet.planning.service.personService.LocalStudentService;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import fr.univtln.projet.planning.service.personService.StaffDRIService;
import fr.univtln.projet.planning.service.planningService.CourseService;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * ServiceRegistry initializes and manages all application services.
 * Call ServiceRegistry.initialize() once at application startup,
 * then use getters to access services throughout the app.
 */
public class ServiceRegistry {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    // ========== Infrastructure Services ==========
    private static CampusService campusService;
    private static BuildingService buildingService;
    private static RoomService roomService;

    // ========== Person Services ==========
    private static AdminService adminService;
    private static ProfessorService professorService;
    private static LocalStudentService localStudentService;
    private static InternationalStudentService internationalStudentService;
    private static StaffDRIService staffDRIService;

    // ========== Academic Services ==========
    private static UFRService ufrService;
    private static PromoService promoService;
    private static GroupService groupService;

    // ========== Planning Services ==========
    private static ModuleService moduleService;
    private static CourseService courseService;

    /**
     * Initializes all services and repositories.
     * Must be called once at application startup.
     */
    public static void initialize() {
        System.out.println("Initializing ServiceRegistry...");

        try {
            // Step 1: Initialize JPA context
            emf = Persistence.createEntityManagerFactory("HyperplanningPU");
            em = emf.createEntityManager();
            System.out.println("JPA EntityManager initialized");

            // Step 2: Initialize all repositories
            CampusRepository campusRepo = new CampusRepository(em);
            BuildingRepository buildingRepo = new BuildingRepository(em);
            RoomRepository roomRepo = new RoomRepository(em);
            AdminRepository adminRepo = new AdminRepository(em);
            ProfessorRepository professorRepo = new ProfessorRepository(em);
            LocalStudentRepository localStudentRepo = new LocalStudentRepository(em);
            InternationalStudentRepository internationalStudentRepo = new InternationalStudentRepository(em);
            StaffDRIRepository staffDRIRepo = new StaffDRIRepository(em);
            UFRRepository ufrRepo = new UFRRepository(em);
            PromoRepository promoRepo = new PromoRepository(em);
            GroupRepository groupRepo = new GroupRepository(em);
            ModuleRepository moduleRepo = new ModuleRepository(em);
            CourseRepository courseRepo = new CourseRepository(em);
            System.out.println("All repositories initialized");

            // Step 3: Initialize simple services (no complex dependencies)
            campusService = new CampusService(campusRepo);
            adminService = new AdminService(adminRepo);
            System.out.println("Simple services initialized (Campus, Admin)");

            // Step 4: Initialize services with single dependencies
            ufrService = new UFRService(ufrRepo, adminService, campusService);
            professorService = new ProfessorService(professorRepo);
            localStudentService = new LocalStudentService(localStudentRepo, promoService);
            internationalStudentService = new InternationalStudentService(internationalStudentRepo);
            staffDRIService = new StaffDRIService(staffDRIRepo);
            System.out.println("Person and UFR services initialized");

            // Step 5: Initialize infrastructure services (depend on UFR, Campus)
            buildingService = new BuildingService(buildingRepo, campusService, ufrService);
            roomService = new RoomService(roomRepo, buildingService);
            System.out.println("Infrastructure services initialized (Building, Room)");

            // Step 6: Initialize academic services (depend on UFR)
            promoService = new PromoService(promoRepo, ufrService);
            groupService = new GroupService(groupRepo, promoService, moduleService);
            System.out.println("Academic services initialized (Promo, Group)");

            // Step 7: Initialize planning services
            moduleService = new ModuleService(moduleRepo, professorService);
            courseService = new CourseService(courseRepo);
            System.out.println("Planning services initialized (Module, Course)");

            System.out.println("ServiceRegistry fully initialized successfully!");

        } catch (Exception e) {
            System.err.println("ERROR during ServiceRegistry initialization:");
            throw new RuntimeException("Failed to initialize ServiceRegistry", e);
        }
    }

    /**
     * Gracefully shuts down the service registry and JPA context.
     * Call this when the application terminates.
     */
    public static void shutdown() {
        System.out.println("Shutting down ServiceRegistry...");
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        System.out.println("ServiceRegistry shutdown complete");
    }

    // ========== GETTERS: Infrastructure Services ==========
    public static CampusService getCampusService() {
        validateInitialized();
        return campusService;
    }

    public static BuildingService getBuildingService() {
        validateInitialized();
        return buildingService;
    }

    public static RoomService getRoomService() {
        validateInitialized();
        return roomService;
    }

    // ========== GETTERS: Person Services ==========
    public static AdminService getAdminService() {
        validateInitialized();
        return adminService;
    }

    public static ProfessorService getProfessorService() {
        validateInitialized();
        return professorService;
    }

    public static LocalStudentService getLocalStudentService() {
        validateInitialized();
        return localStudentService;
    }

    public static InternationalStudentService getInternationalStudentService() {
        validateInitialized();
        return internationalStudentService;
    }

    public static StaffDRIService getStaffDRIService() {
        validateInitialized();
        return staffDRIService;
    }

    // ========== GETTERS: Academic Services ==========
    public static UFRService getUFRService() {
        validateInitialized();
        return ufrService;
    }

    public static PromoService getPromoService() {
        validateInitialized();
        return promoService;
    }

    public static GroupService getGroupService() {
        validateInitialized();
        return groupService;
    }

    // ========== GETTERS: Planning Services ==========
    public static ModuleService getModuleService() {
        validateInitialized();
        return moduleService;
    }

    public static CourseService getCourseService() {
        validateInitialized();
        return courseService;
    }

    // ========== GETTERS: JPA Context ==========
    public static EntityManager getEntityManager() {
        validateInitialized();
        return em;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        validateInitialized();
        return emf;
    }

    // ========== Helper Methods ==========
    private static void validateInitialized() {
        if (emf == null || em == null) {
            throw new IllegalStateException(
                    "ServiceRegistry not initialized! Call ServiceRegistry.initialize() at application startup."
            );
        }
    }
}
