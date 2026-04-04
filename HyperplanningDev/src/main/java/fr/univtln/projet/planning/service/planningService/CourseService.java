package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.mapper.planning.CourseMapper;
import fr.univtln.projet.planning.mapper.planning.ModuleMapper;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class CourseService {

    private final CourseRepository courseRepository;
//    private final ModuleService moduleService;
//    private final RoomService roomService;
//    private final ProfessorService professorService;
    private final GroupRepository groupRepository;

    public CourseService(CourseRepository courseRepository//,
                         //ModuleService moduleService,
                         //RoomService roomService//,
                         /*ProfessorService professorService*/,
                         GroupRepository groupRepository) {
        this.courseRepository = courseRepository;
//        this.moduleService = moduleService;
//        this.roomService = roomService;
//        this.professorService = professorService;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public CourseEntity create(CourseEntity entity) {
        Course jpa = CourseMapper.toJpa(entity);
        checkAllConstraints(jpa);
        Course saved = courseRepository.save(jpa);
        return CourseMapper.toDomain(saved);
    }

    private void checkAllConstraints(Course course) {
        checkNoOverlap(course); // inter-groupe
        checkPromoGroupConflicts(course); // inter-groupes promo
        checkRoomAvailability(course);
        checkTeacherAvailability(course);
    }

    private void checkNoOverlap(Course course) {
        LocalTime newStart = course.getStartTime();
        LocalTime newEnd = newStart.plusMinutes(course.getDuration().toMinutes());

        for (Group group : course.getGroups()) {

            List<Course> existingCourses =
                    courseRepository.findByGroupIdAndDate(
                            group.getGroupId(),
                            course.getDate()
                    );

            for (Course existing : existingCourses) {

                // éviter de comparer avec lui-même (cas update)
                if (course.getCourseId() != null &&
                        course.getCourseId().equals(existing.getCourseId())) {
                    continue;
                }

                LocalTime existingStart = existing.getStartTime();
                LocalTime existingEnd = existingStart.plusMinutes(existing.getDuration().toMinutes());

                boolean overlap =
                        newStart.isBefore(existingEnd) &&
                                existingStart.isBefore(newEnd);

                if (overlap) {
                    throw new IllegalArgumentException(
                            "Overlap detected for group " + group.getGroupId()
                    );
                }
            }
        }
    }

    private void checkRoomAvailability(Course course) {
        if (course.getRoom() == null) return;

        LocalTime newStart = course.getStartTime();
        LocalTime newEnd = newStart.plusMinutes(course.getDuration().toMinutes());

        List<Course> courses =
                courseRepository.findByRoomIdAndDate(
                        course.getRoom().getIdRoom(),
                        course.getDate()
                );

        for (Course c : courses) {

            if (course.getCourseId() != null &&
                    course.getCourseId().equals(c.getCourseId())) {
                continue;
            }

            if (course.overlapsWith(c)) {
                throw new IllegalArgumentException(
                        "Room already occupied at this time"
                );
            }
        }
    }

    private void checkTeacherAvailability(Course course) {

        if (course.getProfessors() == null || course.getProfessors().isEmpty()) {
            return;
        }

        LocalTime newStart = course.getStartTime();
        LocalTime newEnd = newStart.plusMinutes(course.getDuration().toMinutes());

        for (Professor teacher : course.getProfessors()) {

            List<Course> courses =
                    courseRepository.findByTeacherIdAndDate(
                            teacher.getUserId(),
                            course.getDate()
                    );

            for (Course c : courses) {

                if (course.getCourseId() != null &&
                        course.getCourseId().equals(c.getCourseId())) {
                    continue;
                }

                if (course.overlapsWith(c)) {
                    throw new IllegalArgumentException(
                            "Teacher " + teacher.getUserId() + " has a time conflict"
                    );
                }
            }
        }
    }

    private void checkPromoGroupConflicts(Course course) {

        if (course.getGroups() == null || course.getGroups().isEmpty()) {
            return;
        }

        // On suppose qu’un cours appartient à une seule promo via ses groupes
        Group anyGroup = course.getGroups().iterator().next();
        Long promoId = anyGroup.getPromo().getPromoId();

        List<Group> promoGroups = groupRepository.findGroupsByPromoId(promoId);

        LocalTime newStart = course.getStartTime();
        LocalTime newEnd = newStart.plusMinutes(course.getDuration().toMinutes());

        for (Group group : promoGroups) {

            List<Course> courses =
                    courseRepository.findByGroupIdAndDate(
                            group.getGroupId(),
                            course.getDate()
                    );

            for (Course c : courses) {

                if (course.getCourseId() != null &&
                        course.getCourseId().equals(c.getCourseId())) {
                    continue;
                }

                if (course.overlapsWith(c)) {
                    throw new IllegalArgumentException(
                            "Conflict detected across groups of the same promo (groupId=" +
                                    group.getGroupId() + ")"
                    );
                }
            }
        }
    }

    // simple version on universal update to be tested
    @Transactional
    public CourseEntity update(Long id, CourseEntity entity) {
        Course existing = courseRepository.findById(id)
                .orElseThrow();
        Course updated = CourseMapper.toJpa(entity);
        updated.setCourseId(id); // important, sets id of existing course on that one with updated information and
        // so, after save in reality the old course will be updated (
        checkAllConstraints(updated);
        Course saved = courseRepository.save(updated);
        return CourseMapper.toDomain(saved);
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