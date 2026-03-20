package fr.univtln.projet.planning.service.academicService;

import java.util.List;
import java.util.stream.Collectors;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
import fr.univtln.projet.planning.service.personService.LocalStudentService;
import fr.univtln.projet.planning.service.planningService.CourseService; 

public class GroupService {

    /*
    private final GroupRepository groupRepository;
    private final PromoService promoService;
    private final CourseService courseService;
    private final LocalStudentService localStudentService;

    public GroupService(GroupRepository groupRepository, PromoService promoService, CourseService courseService, LocalStudentService localStudentService) {
        this.groupRepository = groupRepository;
        this.promoService = promoService;
        this.courseService = courseService;
        this.localStudentService = localStudentService;
    }

    // --- MÉTHODES MÉTIER ---

    public GroupEntity createGroup(GroupEntity domainGroup) {
        Group jpaEntity = toEntity(domainGroup);
        Group savedEntity = groupRepository.save(jpaEntity);
        return toDomain(savedEntity);
    }

    public GroupEntity getGroupById(Long id) {
        return groupRepository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    public List<GroupEntity> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // --- MAPPINGS ---
    private GroupEntity toDomainEntity(Group jpaEntity) {
        if (jpaEntity == null) return null;

        GroupEntity domainGroup = GroupEntity.GroupFactory(jpaEntity.getNum(), jpaEntity.getType());
        domainGroup.setPromo(promoService.toDomainEntity(jpaEntity.getPromo()));

        if (jpaEntity.getStudents() != null) {
            jpaEntity.getStudents().forEach(s -> domainGroup.addLocalStudent(localStudentService.toDomainEntity(s)));
        }
        if (jpaEntity.getPlanning() != null) {
            jpaEntity.getPlanning().forEach(c -> domainGroup.addCourse(courseService.toDomainEntity(c)));
        }
        return domainGroup;
    }

    private Group toEntityEntity(GroupEntity domainGroup) {
        if (domainGroup == null) return null;

        Group jpaEntity = Group.GroupFactory(domainGroup.getNum(), domainGroup.getType());
        jpaEntity.setPromo(promoService.toJpaModel(domainGroup.getPromo()));

        if (domainGroup.getStudents() != null) {
            domainGroup.getStudents().forEach(s -> jpaEntity.addLocalStudent(localStudentService.toJpaModel(s)));
        }
        if (domainGroup.getPlanning() != null) {
            domainGroup.getPlanning().forEach(c -> jpaEntity.addCourse(courseService.toJpaModel(c)));
        }
        return jpaEntity;
    }

     */
}