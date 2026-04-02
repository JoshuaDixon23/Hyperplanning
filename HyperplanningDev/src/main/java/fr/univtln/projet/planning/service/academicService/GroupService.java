package fr.univtln.projet.planning.service.academicService;

import java.util.List;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.mapper.academic.GroupMapper;
import fr.univtln.projet.planning.mapper.planning.ModuleMapper;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.academic.GroupType;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import jakarta.transaction.Transactional;

public class GroupService {

    private final GroupRepository groupRepository;
    private final PromoService promoService;

    public GroupService(GroupRepository groupRepository,
                        PromoService promoService,
                        ModuleService moduleService) {
        this.groupRepository = groupRepository;
        this.promoService = promoService;
    }

    // ------------------ CREATE ------------------

    public GroupEntity create(GroupEntity entity) {
        Group saved = groupRepository.save(GroupMapper.toJpa(entity));
        return GroupMapper.toDomain(saved);
    }

    public GroupEntity create(int num, GroupType type, String promoName, StudyLevel promoStudyLevel, int promoYear){
        Promo promo = promoService.findJpaByNameAndYearAndStudyLevel(promoName, promoYear, promoStudyLevel);
        Group group = Group.GroupFactory(num, type);
        group.setPromo(promo);
        Group saved = groupRepository.save(group);
        return GroupMapper.toDomain(saved);
    }

    // ------------------ FIND ------------------

    public GroupEntity findById(Long id) {
        return groupRepository.findById(id)
                .map(GroupMapper::toDomain)
                .orElse(null);
    }

    public List<GroupEntity> findAll() {
        return groupRepository.findAll()
                .stream()
                .map(GroupMapper::toDomain)
                .toList();
    }

    public Group findJpaByNumAndTypeAndPromo(int num, GroupType type, String promoName,
                                                StudyLevel promoStudyLevel, int promoYear){
        Promo promo =  promoService.findJpaByNameAndYearAndStudyLevel(promoName, promoYear, promoStudyLevel);
        return groupRepository.findByNumAndTypeAndPromo(num, type, promo);
    }

    @Transactional
    public void addModuleToGroup(GroupEntity groupEntity, ModuleEntity moduleEntity) {
        if (groupEntity == null || moduleEntity == null) {
            throw new IllegalArgumentException("Group and Module cannot be null");
        }

        Group groupJpa = findJpaByNumAndTypeAndPromo(
                groupEntity.getNum(),
                groupEntity.getType(),
                groupEntity.getPromo().getName(),
                groupEntity.getPromo().getStudyLevel(),
                groupEntity.getPromo().getYear()
        );

        if (groupJpa == null) {
            throw new IllegalStateException("Group not found for module association");
        }

        Module moduleJpa = ModuleMapper.toJpa(moduleEntity);
        groupJpa.addModule(moduleJpa);
        groupRepository.save(groupJpa);
    }

    // is not tested, may require change of signature in ordrer to have comfortable insertion
    public void addModuleToGroup(Long groupId, ModuleEntity moduleEntity) {
        Group group = groupRepository.findById(groupId).orElseThrow();
        Module module = ModuleMapper.toJpa(moduleEntity);
        group.addModule(module);
        groupRepository.save(group);
    }
}