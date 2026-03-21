package fr.univtln.projet.planning.service.planningService;

import org.junit.jupiter.api.*;

class CourseServiceTest {

    @Test
    void create_then_findAll_should_work() {
        /*
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
        ModuleService moduleService = new ModuleService(ModuleRepository, );

        CourseService service = new CourseService(courseRepository,ModuleRepository,moduleService);

        // service.create(course);



        List<CourseEntity> planning = service.findAll(1,100);
        planning.forEach(System.out::println);

        em.close();
        emf.close();

         */
    }
}
