package fr.univtln.projet.planning.service.courseService;

import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.modele.planning.CourseType;
import fr.univtln.projet.planning.modele.planning.Language;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.personRepository.LocalStudentRepository;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.service.planningService.CourseService;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

class CourseServiceTest {

    @Test
    void create_then_findAll_should_work() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");

        String firstName = "elisAbetH";
        String lastName = "MurisAsco";


        // Act (action)
        ProfessorEntity professor = ProfessorEntity.ProfessorFactory(firstName, lastName);

        ModuleEntity module = ModuleEntity.builder()
                .code("UE123")
                .name("developpement avancé")
                .ECTS(1)
                .responsible(professor)
                .language(Language.FRENCH)
                .build();

        System.out.println("coucou");

        CourseEntity course = CourseEntity.builder()
                .date(LocalDate.of(2026, 3, 20))
                .startTime(LocalTime.of(10, 0))
                .duration(Duration.ofHours(2))
                .courseType(CourseType.CM)
                .professor(professor)
                .module(module)
                .build();

        System.out.println("coucou");


        EntityManager em = emf.createEntityManager();

        CourseRepository courseRepository = new CourseRepository(em);
        ModuleRepository ModuleRepository = new ModuleRepository(em);
        ModuleService moduleService = new ModuleService(ModuleRepository);

        CourseService service = new CourseService(courseRepository,ModuleRepository,moduleService);

        // service.create(course);



        List<CourseEntity> planning = service.findAll(1,100);
        planning.forEach(System.out::println);

        em.close();
        emf.close();
    }
}
