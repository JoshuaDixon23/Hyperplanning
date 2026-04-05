package fr.univtln.projet.planning.service.planningService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.mapper.planning.CourseMapper;
import fr.univtln.projet.planning.mapper.planning.ModuleMapper;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.service.academicService.GroupService;
import fr.univtln.projet.planning.service.infrastructureService.RoomService;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import jakarta.transaction.Transactional;

public class CourseService {

    private final CourseRepository courseRepository;

    private final ModuleService moduleService;

    private final RoomService roomService;
    private final ProfessorService professorService;
    private final GroupService groupService;

    public CourseService(
            CourseRepository courseRepository,
            ModuleService moduleService,
            RoomService roomService,
            ProfessorService professorService,
            GroupService groupService) {
        
        this.courseRepository = courseRepository;
        this.moduleService = moduleService;
        this.roomService = roomService;
        this.professorService = professorService;
        this.groupService = groupService;
    }

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        this.moduleService = null;
        this.roomService = null;
        this.professorService = null;
        this.groupService = null;
    }

    @Transactional
    public void create(CourseEntity courseEntity) {
        Module moduleJpa = null;
        if (courseEntity.getModule() != null) {
            moduleJpa = moduleService.findJpaByCode(courseEntity.getModule().getCode());
            if (moduleJpa == null) {
                throw new IllegalArgumentException("Module introuvable en base");
            }
        }
        
        Course courseJpa = Course.builder()
                .date(courseEntity.getDate())
                .startTime(courseEntity.getStartTime())
                .duration(courseEntity.getDuration())
                .courseType(courseEntity.getCourseType())
                .module(moduleJpa)
                .build();

        

        if (courseEntity.getRoom() != null) {
            Room roomJpa = roomService.findByNumber(courseEntity.getRoom().getNumber());
            courseJpa.setRoom(roomJpa);
        }

        if (courseEntity.getGroups() != null) {
            courseEntity.getGroups().forEach(groupEntity -> {
                Group groupJpa = groupService.findJpaByNumAndTypeAndPromo(groupEntity.getNum(), groupEntity.getType(), groupEntity.getPromo().getName() ,groupEntity.getPromo().getStudyLevel(), groupEntity.getPromo().getYear());
                courseJpa.addGroup(groupJpa);
            });
        }

        if (courseEntity.getProfessors() != null) {
            courseEntity.getProfessors().forEach(profEntity -> {
                Professor profJpa = professorService.findJpaByEmailUniv(profEntity.getEmailUniv());
                courseJpa.addProfessor(profJpa);
            });
        }

        courseRepository.save(courseJpa);
    }

    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course introuvable"));
        courseRepository.delete(course);
    }

    public Optional<CourseEntity> findById(Long id) {
        return courseRepository.findById(id).map(CourseMapper::toDomain);
    }

    public List<CourseEntity> findAll() {
        return courseRepository.findAll().stream().map(CourseMapper::toDomain).toList();
    }

    // ################### PLANNING ##################

    public List<CourseEntity> findPlanningByGroup(Group group, LocalDate start, LocalDate end) {
        return courseRepository.findByGroupAndPeriod(group, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByGroupId(Long groupId, LocalDate start, LocalDate end) {
        return courseRepository.findByGroupIdAndPeriod(groupId, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByModule(Module module, LocalDate start, LocalDate end) {
        return courseRepository.findByModuleAndPeriod(module, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByModule(ModuleEntity module, LocalDate start, LocalDate end) {
        Module m = ModuleMapper.toJpa(module);
        return courseRepository.findByModuleAndPeriod(m, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByModuleCode(String code, LocalDate start, LocalDate end) {
        return courseRepository.findByModuleCodeAndPeriod(code, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByRoomId(Long roomId, LocalDate start, LocalDate end) {
        return courseRepository.findByRoomIdAndPeriod(roomId, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByProfessor(Professor professor, LocalDate start, LocalDate end) {
        return courseRepository.findByProfessorAndPeriod(professor, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByProfessorId(Long professorId, LocalDate start, LocalDate end) {
        return courseRepository.findByProfessorIdAndPeriod(professorId, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByStudentId(Long studentId, LocalDate start, LocalDate end) {
        return courseRepository.findByStudentIdAndPeriod(studentId, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> getPlanningByStudentEmailUniv(String emailUniv, LocalDate start, LocalDate end) {
        return courseRepository.findByStudentEmailUnivAndPeriod(emailUniv, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }

    public List<CourseEntity> findPlanningByPromoId(Long promoId, LocalDate start, LocalDate end) {
        return courseRepository.findByPromoIdAndPeriod(promoId, start, end)
                .stream().map(CourseMapper::toDomain).toList();
    }
}