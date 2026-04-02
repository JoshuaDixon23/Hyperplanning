package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.planning.CourseType;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.BuildingRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.RoomRepository;
import fr.univtln.projet.planning.repository.personRepository.ProfessorRepository;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
import fr.univtln.projet.planning.service.academicService.PromoService;
import fr.univtln.projet.planning.service.academicService.UFRService;
import fr.univtln.projet.planning.service.infrastructureService.BuildingService;
import fr.univtln.projet.planning.service.infrastructureService.CampusService;
import fr.univtln.projet.planning.service.infrastructureService.RoomService;
import fr.univtln.projet.planning.service.personService.AdminService;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import fr.univtln.projet.planning.service.academicService.GroupService;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class CourseServiceTest {

//    @Test
//    void testCreateCourseComplete() {
//        // ------------------ INIT JPA ------------------
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
//        EntityManager em = emf.createEntityManager();
//
//        // ------------------ REPOSITORIES ------------------
//        ModuleRepository moduleRepo = new ModuleRepository(em);
//        BuildingRepository buildingRepo = new BuildingRepository(em);
//        RoomRepository roomRepo = new RoomRepository(em);
//        ProfessorRepository profRepo = new ProfessorRepository(em);
//        GroupRepository groupRepo = new GroupRepository(em);
//        CourseRepository courseRepo = new CourseRepository(em);
//        PromoRepository promoRepo = new PromoRepository(em);
//        UFRRepository ufrRepo = new UFRRepository(em);
//        AdminRepository adminRepo = new AdminRepository(em);
//        CampusRepository campusRepo = new CampusRepository(em);
//
//        // ------------------ SERVICES ------------------
//        CampusService campusService = new CampusService(campusRepo);
//        AdminService adminService = new AdminService(adminRepo);
//        UFRService ufrService = new UFRService(ufrRepo, adminService, campusService);
//        BuildingService buildingService = new BuildingService(buildingRepo, campusService, ufrService);
//
//        PromoService promoService = new PromoService(promoRepo, ufrService);
//        ProfessorService profService = new ProfessorService(profRepo);
//        ModuleService moduleService = new ModuleService(moduleRepo, profService);
//        GroupService groupService = new GroupService(groupRepo, promoService, moduleService);
//
//        RoomService roomService = new RoomService(roomRepo, buildingService);
//
//        CourseService courseService = new CourseService(courseRepo/*, moduleService, roomService, profService*/);
//
//        // ------------------ CREATE COURSE ENTITY VIA KEYS ------------------
//        LocalDate date = LocalDate.of(2026, 4, 22);
//        LocalTime start = LocalTime.of(9, 0);
//        Duration duration = Duration.ofHours(2);
//        CourseType type = CourseType.CM;
//
//        CourseEntity course = CourseEntity.builder()
//                .module(moduleService.findByCode("M-JAVA-01"))
//                .date(date)
//                .startTime(start)
//                .duration(duration)
//                .courseType(type)
//                .build();
//
//        // ------------------ PERSIST ------------------
//        courseService.create(course);
//
//        // ------------------ VERIFY ------------------
//        List<CourseEntity> courses = courseService.findAll();
//        courses.forEach(System.out::println);
//
//        LocalDate begin = LocalDate.of(2026, 1, 1);
//        LocalDate end = LocalDate.of(2026, 12, 23);
//        List<CourseEntity> coursesStud = courseService.findPlanningByStudentId(3L, begin, end);
//        System.out.println("Couses of student");
//        coursesStud.forEach(System.out::println);
//
//        List<CourseEntity> coursesPromo = courseService.findPlanningByPromoId(1L, begin, end);
//        System.out.println("Couses of promotion");
//        coursesPromo.forEach(System.out::println);
//
//        List<CourseEntity> coursesProf = courseService.findPlanningByProfessorId(2L, begin, end);
//        System.out.println("Couses of professor");
//        coursesProf.forEach(System.out::println);
//
//        em.close();
//        emf.close();
//    }

    @Test
    void create(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        ModuleRepository moduleRepo = new ModuleRepository(em);
        ProfessorRepository professorRepo = new ProfessorRepository(em);
        ProfessorService professorService = new ProfessorService(professorRepo);
        ModuleService moduleRepository = new ModuleService(moduleRepo, professorService);
        System.out.println(professorRepo.findAll());
        /*
        ModuleEntity module = moduleRepository.findByCode("DATA10");
        CourseEntity course = CourseEntity.builder()
                .module(module)
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalTime.now().minusHours(10))
                .duration(Duration.ofHours(1))
                .courseType(CourseType.CM)
                .build();

        CourseRepository courseRepo = new CourseRepository(em);
        GroupRepository groupRepo = new GroupRepository(em);
        CourseService courseService = new CourseService(courseRepo, groupRepo);
        CourseEntity saved = courseService.create(course);
        System.out.println(saved);

         */
    }
}