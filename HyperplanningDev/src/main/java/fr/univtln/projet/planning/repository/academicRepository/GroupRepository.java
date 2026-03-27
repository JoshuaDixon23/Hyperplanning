package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.academic.GroupType;
import fr.univtln.projet.planning.modele.academic.Promo;
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

    public List<Group> findByType(GroupType type) {
        String jpql = "SELECT g FROM Group g WHERE g.type = :type";
        TypedQuery<Group> query = em.createQuery(jpql, Group.class);
        query.setParameter("type", type);
        return query.getResultList();
    }

    public Group findByNumAndTypeAndPromo(int num, GroupType type, Promo promo) {
        String jpql = "SELECT g FROM Group g WHERE g.num = :num AND g.type = :type AND g.promo = :promo";
        TypedQuery<Group> query = em.createQuery(jpql, Group.class);
        query.setParameter("num", num);
        query.setParameter("type", type);
        query.setParameter("promo", promo);
        return query.getSingleResult();
    }
}