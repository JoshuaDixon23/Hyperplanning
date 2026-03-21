package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
import fr.univtln.projet.planning.service.planningService.CourseService;
import fr.univtln.projet.planning.service.planningService.ModuleService;

import java.util.List;

public class GroupService {
    /*
    private final GroupRepository groupRepository;
    private final PromoService promoService;
    private final CourseService courseService;
    private final ModuleService moduleService;

    public GroupService(GroupRepository groupRepository,
                        PromoService promoService,
                        CourseService courseService,
                        ModuleService moduleService) {
        this.groupRepository = groupRepository;
        this.promoService = promoService;
        this.courseService = courseService;
        this.moduleService = moduleService;
    }

    // ------------------ MAPPERS ------------------

    public GroupEntity toDomain(Group g) {
        if (g == null) return null;

        GroupEntity entity = GroupEntity.GroupFactory(
                g.getNum(),
                g.getType()
        );

        entity.setPromo(promoService.toDomain(g.getPromo()));

        if (g.getStudents() != null) {
            g.getStudents().forEach(s -> entity.addLocalStudent(localStudentService.toDomain(s)));
        }

        if (g.getPlanning() != null) {
            g.getPlanning().forEach(c -> entity.addCourse(courseService.toDomain(c)));
        }

        return entity;
    }

    public Group toJpa(GroupEntity g) {
        if (g == null) return null;

        Group jpa = Group.GroupFactory(
                g.getNum(),
                g.getType()
        );

        jpa.setPromo(promoService.toJpa(g.getPromo()));

        if (g.getStudents() != null) {
            g.getStudents().forEach(s -> jpa.addLocalStudent(localStudentService.toJpa(s)));
        }

        if (g.getPlanning() != null) {
            g.getPlanning().forEach(c -> jpa.addCourse(courseService.toJpa(c)));
        }

        return jpa;
    }

    // ------------------ CREATE ------------------

    public GroupEntity create(GroupEntity entity) {
        Group saved = groupRepository.save(toJpa(entity));
        return toDomain(saved);
    }

    // ------------------ FIND ------------------

    public GroupEntity findById(Long id) {
        return groupRepository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    public List<GroupEntity> findAll() {
        return groupRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

     */
}