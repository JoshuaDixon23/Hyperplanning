package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class GroupRepository extends JpaRepository<Group, Long> {

    // Plus besoin de passer la classe en paramètre, on la donne directement au super()
    public GroupRepository(EntityManager entityManager) {
        super(Group.class, entityManager);
    }

    // findAll() sans pagination
    public List<Group> findAll() {
        String jpql = "SELECT g FROM Group g";
        TypedQuery<Group> query = em.createQuery(jpql, Group.class);
        return query.getResultList();
    }

    public List<Group> findByType(fr.univtln.projet.planning.modele.academic.GroupType type) {
        String jpql = "SELECT g FROM Group g WHERE g.type = :type";
        TypedQuery<Group> query = em.createQuery(jpql, Group.class);
        query.setParameter("type", type);
        return query.getResultList();
    }
}