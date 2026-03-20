package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    // ###################MAPPERS##################
    // =========================

    // JPA -> Entity
    private CourseEntity toDomain(Course c) {
        if (c == null) return null;

        CourseEntity.Builder builder = CourseEntity.builder()
                .date(c.getDate())
                .startTime(c.getStartTime())        // pb de classe
                .duration(c.getDuration())
                .courseType(c.getCourseType());


        // builder.module(...);


        // builder.room(...);


        // c.getProfessors().forEach(builder::professor);

        return builder.build();
    }

    // Entity -> JPA
    private Course toJpa(CourseEntity c) {
        if (c == null) return null;

        Course.Builder builder = Course.builder()
                .date(c.getDate())
                .startTime(c.getStartTime())        // pb de classe
                .duration(c.getDuration())
                .courseType(c.getCourseType());


        // course.setModule(...);


        // course.setRoom(...);


        // course.setProfessors(...);

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

        // Validation métier
        validateCourse(entity);

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

    // =========================
    // PLANNING
    // =========================

    public List<CourseEntity> getPlanningByGroup(Long groupId, LocalDate start, LocalDate end) {
        validatePeriod(start, end);

        return courseRepository.getPlanningByGroup(groupId, start, end)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public List<CourseEntity> getPlanningByModule(String code, LocalDate start, LocalDate end) {
        validatePeriod(start, end);

        return courseRepository.getPlanningByModule(code, start, end)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public List<CourseEntity> getPlanningByRoom(Long roomId, LocalDate start, LocalDate end) {
        validatePeriod(start, end);

        return courseRepository.getPlanningByRoom(roomId, start, end)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    // =========================
    // VALIDATION MÉTIER
    // =========================

    private void validateCourse(CourseEntity c) {
        if (c == null) {
            throw new IllegalArgumentException("Course null");
        }

        if (c.getStartTime() == null) {
            throw new IllegalArgumentException("StartTime requis");
        }

        if (c.getDuration() == null || c.getDuration().isZero()) {
            throw new IllegalArgumentException("Duration invalide");
        }

        if (c.getCourseType() == null) {
            throw new IllegalArgumentException("CourseType requis");
        }

        if (c.getModule() == null) {
            throw new IllegalArgumentException("Module requis");
        }
    }

    private void validatePeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates nulles");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Période invalide");
        }
    }
}