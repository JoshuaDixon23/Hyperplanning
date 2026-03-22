package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.modele.planning.CourseType;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.service.infrastructureService.RoomService;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

public class CourseService {

    /*
    private final CourseRepository courseRepository;
    private final ModuleService moduleService;
    private final RoomService roomService;
    private final ProfessorService professorService;

    public CourseService(CourseRepository courseRepository, ModuleService moduleService,
    RoomService roomService, ProfessorService professorService) {
        this.courseRepository = courseRepository;
        this.moduleService = moduleService;
        this.roomService = roomService;
        this.professorService = professorService;
    }

    // ###################MAPPERS##################

    // JPA -> Entity
    public CourseEntity toDomain(Course c) {
        if (c == null) return null;

        CourseEntity.Builder builder = CourseEntity.builder()
                .date(c.getDate())
                .startTime(c.getStartTime())        // pb de classe - quoi ?
                .duration(c.getDuration())
                .courseType(c.getCourseType())
                .module(moduleService.toDomain(c.getModule()))
                .room(roomService.toDomain(c.getRoom()));

        if (c.getProfessors() != null) {
            c.getProfessors().forEach(prof ->
                    builder.professor(professorService.toDomain(prof))
            );
        }

        return builder.build();
    }

    // Entity -> JPA
    public Course toJpa(CourseEntity c) {
        if (c == null) return null;


        Course.Builder builder = Course.builder()
                .module(moduleService.toJpa(c.getModule()))
                .date(c.getDate())
                .startTime(c.getStartTime())
                .duration(c.getDuration())
                .courseType(c.getCourseType())
                .room(roomService.toJpa(c.getRoom()));

        if (c.getProfessors() != null) {
            c.getProfessors().forEach(prof ->
                    builder.professor(professorService.toJpa(prof))
            );
        }


        return builder.build();
    }


    // ################### CRUD #########################


    public Optional<CourseEntity> findById(Long id) {
        return courseRepository.findById(id)
                .map(this::toDomain);
    }

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

    // create course with no room and no
    @Transactional
    public CourseEntity create(LocalDate date, LocalTime startTime, Duration duration, CourseType courseType,
                               String codeModule) {
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

    /*
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

     */

    // VALIDATION MÉTIER






}