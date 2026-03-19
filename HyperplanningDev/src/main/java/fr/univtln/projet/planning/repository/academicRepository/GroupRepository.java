package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class GroupRepository extends JpaRepository<Group, Long > {


    protected GroupRepository(Class<Group> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    // Sauvegarder ou mettre à jour un groupe
    public Group save(Group group) {
        super.em.getTransaction().begin();
        if (group.getGroupId() == null) {
            super.em.persist(group); // Nouveau
        } else {
            group = super.em.merge(group); // Mise à jour
        }
        super.em.getTransaction().commit();
        return group;
    }

    // Trouver par ID
    public Optional<Group> findById(Long id) {
        return Optional.ofNullable(super.em.find(Group.class, id));
    }

    // Exemple avec une requête JPQL (comme noté sur ton dessin)
    public List<Group> findAll() {
        String jpql = "SELECT g FROM Group g";
        TypedQuery<Group> query = super.em.createQuery(jpql, Group.class);
        return query.getResultList();
    }

    // Autre exemple de JPQL : Trouver par type de groupe
    public List<Group> findByType(fr.univtln.projet.planning.modele.academic.GroupType type) {
        String jpql = "SELECT g FROM Group g WHERE g.type = :type";
        TypedQuery<Group> query = super.em.createQuery(jpql, Group.class);
        query.setParameter("type", type);
        return query.getResultList();
    }
}