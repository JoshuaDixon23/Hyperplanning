package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List; 
import java.util.Optional;

import fr.univtln.projet.planning.modele.academic.Group;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class GroupRepository {

    private final EntityManager entityManager;

    public GroupRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Sauvegarder ou mettre à jour un groupe
    public Group save(Group group) {
        entityManager.getTransaction().begin();
        if (group.getGroupId() == null) {
            entityManager.persist(group); // Nouveau
        } else {
            group = entityManager.merge(group); // Mise à jour
        }
        entityManager.getTransaction().commit();
        return group;
    }

    // Trouver par ID
    public Optional<Group> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Group.class, id));
    }

    // Exemple avec une requête JPQL (comme noté sur ton dessin)
    public List<Group> findAll() {
        String jpql = "SELECT g FROM Group g";
        TypedQuery<Group> query = entityManager.createQuery(jpql, Group.class);
        return query.getResultList();
    }
    
    // Autre exemple de JPQL : Trouver par type de groupe
    public List<Group> findByType(fr.univtln.projet.planning.modele.academic.GroupType type) {
        String jpql = "SELECT g FROM Group g WHERE g.type = :type";
        TypedQuery<Group> query = entityManager.createQuery(jpql, Group.class);
        query.setParameter("type", type);
        return query.getResultList();
    }
}