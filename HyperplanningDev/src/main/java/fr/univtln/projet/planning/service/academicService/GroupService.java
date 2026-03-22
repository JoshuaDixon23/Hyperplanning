package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.academic.GroupType;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
import fr.univtln.projet.planning.service.planningService.CourseService;
import fr.univtln.projet.planning.service.planningService.ModuleService;

import java.util.List;

public class GroupService {

    private final GroupRepository groupRepository;
    private final PromoService promoService;
    private final ModuleService moduleService;

    public GroupService(GroupRepository groupRepository,
                        PromoService promoService,
                        ModuleService moduleService) {
        this.groupRepository = groupRepository;
        this.promoService = promoService;
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
        return entity;
    }

    public Group toJpa(GroupEntity g) {
        if (g == null) return null;
        Group jpa = Group.GroupFactory(
                g.getNum(),
                g.getType()
        );
        jpa.setPromo(promoService.toJpa(g.getPromo()));
        return jpa;
    }

    // ------------------ CREATE ------------------

    public GroupEntity create(GroupEntity entity) {
        Group saved = groupRepository.save(toJpa(entity));
        return toDomain(saved);
    }

    public GroupEntity create(int num, GroupType type, String promoName, StudyLevel promoStudyLevel, int promoYear){
        Promo promo = promoService.findJpaByNameAndYearAndStudyLevel(promoName, promoYear, promoStudyLevel);
        Group group = Group.GroupFactory(num, type);
        group.setPromo(promo);
        Group saved = groupRepository.save(group);
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

    public Group findJpaByNumAndTypeAndPromo(int num, GroupType type, String promoName,
                                                StudyLevel promoStudyLevel, int promoYear){
        Promo promo =  promoService.findJpaByNameAndYearAndStudyLevel(promoName, promoYear, promoStudyLevel);
        return groupRepository.findByNumAndTypeAndPromo(num, type, promo);
    }
}