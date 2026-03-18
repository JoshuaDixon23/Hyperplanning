package fr.univtln.projet.planning.service.academicService;

import java.util.List;
import java.util.stream.Collectors;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.repository.academicRepository.GroupRepository; 

public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
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

    // --- MÉTHODES DE MAPPING (Traduction) ---

    /**
     * Convertit le modèle JPA (Base de données) vers l'objet Métier (PromoEntity)
     */
    private GroupEntity toDomain(Group jpaEntity) {
        if (jpaEntity == null) return null;

        GroupEntity domainGroup = GroupEntity.GroupFactory(jpaEntity.getNum(), jpaEntity.getType());
        

        // depend de StudentRepository ...
        
        return domainGroup;
    }

    /**
     * Convertit l'objet Métier (PromoEntity) vers le modèle JPA (Base de données)
     */
    private Group toEntity(GroupEntity domainGroup) {
        if (domainGroup == null) return null;

        Group jpaEntity = Group.GroupFactory(domainGroup.getNum(), domainGroup.getType());

        // depend de StudentRepository ...
        
        return jpaEntity;
    }
}