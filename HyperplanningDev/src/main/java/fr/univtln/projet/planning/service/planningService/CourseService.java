package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.modele.planning.CourseType;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.service.academicService.GroupService;
import fr.univtln.projet.planning.service.infrastructureService.RoomService;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;

public class CourseService {

    private final CourseRepository courseRepository;
    private final ModuleService moduleService;
    private final RoomService roomService;
    private final ProfessorService professorService;

    public CourseService(CourseRepository courseRepository,
                         ModuleService moduleService,
                         RoomService roomService,
                         ProfessorService professorService) {
        this.courseRepository = courseRepository;
        this.moduleService = moduleService;
        this.roomService = roomService;
        this.professorService = professorService;
    }

    // ################### MAPPERS ##################

    public CourseEntity toDomain(Course c) {
        if (c == null) return null;
        CourseEntity.Builder builder = CourseEntity.builder()
                .module(moduleService.toDomain(c.getModule()))
                .date(c.getDate())
                .startTime(c.getStartTime())
                .duration(c.getDuration())
                .room(roomService.toDomain(c.getRoom()))
                .courseType(c.getCourseType());

        if (c.getProfessors() != null) {
            c.getProfessors().forEach(prof -> builder.professor(professorService.toDomain(prof)));
        }

        return builder.build();
    }

    public Course toJpa(CourseEntity c) {
        if (c == null) return null;
        Course.Builder builder = Course.builder()
                .module(moduleService.toJpa(c.getModule()))
                .date(c.getDate())
                .startTime(c.getStartTime())
                .duration(c.getDuration())
                .room(roomService.toJpa(c.getRoom()))
                .courseType(c.getCourseType());

        if (c.getProfessors() != null) {
            c.getProfessors().forEach(prof -> builder.professor(professorService.toJpa(prof)));
        }

        return builder.build();
    }

    // ################### CRUD ##################

    public Optional<CourseEntity> findById(Long id) {
        return courseRepository.findById(id).map(this::toDomain);
    }

    public List<CourseEntity> findAll() {
        return courseRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Transactional
    public CourseEntity create(CourseEntity entity) {
        Course jpa = toJpa(entity);
        courseRepository.save(jpa);
        return entity;
    }

    /*
    @Transactional
    public CourseEntity create(
            LocalDate date,
            LocalTime startTime,
            Duration duration,
            CourseType courseType,
            String moduleCode,          // code naturel du module
            Long roomId,                // id de la salle
            List<Long> professorIds,    // ids des professeurs
            List<Long> groupIds         // ids des groupes
    ) {
        // 1. Récupération des entités liées
        ModuleEntity module = moduleService.findByCode(moduleCode);

        RoomEntity room = roomService.findById(roomId);

        var professors = professorIds.stream()
                .map(id -> professorService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Professeur introuvable : " + id)))
                .toList();

        var groups = groupIds.stream()
                .map(id -> moduleService.findGroupById(id) // ou un GroupService si tu as
                        .orElseThrow(() -> new IllegalArgumentException("Groupe introuvable : " + id)))
                .toList();

        // 2. Construction de l'entité
        CourseEntity entity = CourseEntity.builder()
                .module(module)
                .date(date)
                .startTime(startTime)
                .duration(duration)
                .courseType(courseType)
                .room(room)
                .build();

        professors.forEach(entity::professor); // ajoute les profs
        groups.forEach(entity::group);         // ajoute les groupes (il faut ajouter la méthode `group(GroupEntity g)` dans ton builder)

        // 3. Conversion en JPA et persist
        Course jpa = toJpa(entity);
        courseRepository.save(jpa);

        // 4. Retour de l'entité Java pure
        return entity;
    }

     */

    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course introuvable"));
        courseRepository.delete(course);
    }

    // ################### PLANNING ##################

    // By Group
    public List<CourseEntity> findPlanningByGroup(Group group, LocalDate start, LocalDate end) {
        return courseRepository.findByGroupAndPeriod(group, start, end)
                .stream().map(this::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByGroupId(Long groupId, LocalDate start, LocalDate end) {
        return courseRepository.findByGroupIdAndPeriod(groupId, start, end)
                .stream().map(this::toDomain).toList();
    }

    // By Module
    public List<CourseEntity> findPlanningByModule(Module module, LocalDate start, LocalDate end) {
        return courseRepository.findByModuleAndPeriod(module, start, end)
                .stream().map(this::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByModuleCode(String code, LocalDate start, LocalDate end) {
        return courseRepository.findByModuleCodeAndPeriod(code, start, end)
                .stream().map(this::toDomain).toList();
    }

    // By Room
    public List<CourseEntity> findPlanningByRoomId(Long roomId, LocalDate start, LocalDate end) {
        return courseRepository.findByRoomIdAndPeriod(roomId, start, end)
                .stream().map(this::toDomain).toList();
    }

    // By Professor
    public List<CourseEntity> findPlanningByProfessor(Professor professor, LocalDate start, LocalDate end) {
        return courseRepository.findByProfessorAndPeriod(professor, start, end)
                .stream().map(this::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByProfessorId(Long professorId, LocalDate start, LocalDate end) {
        return courseRepository.findByProfessorIdAndPeriod(professorId, start, end)
                .stream().map(this::toDomain).toList();
    }

    // By Student
    public List<CourseEntity> findPlanningByStudentId(Long studentId, LocalDate start, LocalDate end) {
        return courseRepository.findByStudentIdAndPeriod(studentId, start, end)
                .stream().map(this::toDomain).toList();
    }

    public List<CourseEntity> getPlanningByStudentEmailUniv(String emailUniv, LocalDate start, LocalDate end) {
        return courseRepository.findByStudentEmailUnivAndPeriod(emailUniv, start, end)
                .stream().map(this::toDomain).toList();
    }

    // By Promo
    public List<CourseEntity> findPlanningByPromoId(Long promoId, LocalDate start, LocalDate end) {
        return courseRepository.findByPromoIdAndPeriod(promoId, start, end)
                .stream().map(this::toDomain).toList();
    }
}