package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleService moduleService;

    public CourseService(CourseRepository courseRepository, ModuleRepository moduleRepository
    , ModuleService moduleService) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.moduleService = moduleService;
    }


    // ###################MAPPERS##################


    // JPA -> Entity
    private CourseEntity toDomain(Course c) {
        if (c == null) return null;


        CourseEntity.Builder builder = CourseEntity.builder()
                .date(c.getDate())
                .startTime(c.getStartTime())        // pb de classe
                .duration(c.getDuration())
                .courseType(c.getCourseType())
                .module(moduleService.toDomain(c.getModule()));

        return builder.build();
    }

    // Entity -> JPA
    private Course toJpa(CourseEntity c) {
        if (c == null) return null;


        Course.Builder builder = Course.builder()
                .module(moduleService.toJpa(c.getModule()))
                .date(c.getDate())
                .startTime(c.getStartTime())
                .duration(c.getDuration())
                .courseType(c.getCourseType());



        return builder.build();
    }


    // ################### CRUD #########################


    public Optional<CourseEntity> findById(Long id) {
        return courseRepository.findById(id)
                .map(this::toDomain);
    }
    //findAll à definir dans courses repo
    public List<CourseEntity> findAll(int pageNumber, int pageSize) {
        return courseRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Transactional
    public CourseEntity create(CourseEntity entity) {


        // Mapping
        Course jpa = toJpa(entity);

        // Save
        courseRepository.save(jpa);

        return entity;
    }

    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course introuvable"));

        courseRepository.delete(course);
    }


    // PLANNING


    public List<CourseEntity> getPlanningByGroup(Long groupId, LocalDate start, LocalDate end) {


        return courseRepository.getPlanningByGroup(groupId, start, end)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public List<CourseEntity> getPlanningByModule(String code, LocalDate start, LocalDate end) {


        return courseRepository.getPlanningByModule(code, start, end)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public List<CourseEntity> getPlanningByRoom(Long roomId, LocalDate start, LocalDate end) {


        return courseRepository.getPlanningByRoom(roomId, start, end)
                .stream()
                .map(this::toDomain)
                .toList();
    }


    // VALIDATION MÉTIER





}