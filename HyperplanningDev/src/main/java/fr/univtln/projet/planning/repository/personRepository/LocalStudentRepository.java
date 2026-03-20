package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class LocalStudentRepository extends JpaRepository<LocalStudent, Long> {

    public LocalStudentRepository(EntityManager entityManager) {
        super(LocalStudent.class, entityManager);
    }

    public List<LocalStudent> findByPromo(int pageNumber, int pageSize, int promoId) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Pagination invalide");
        }
        return em.createQuery(
                        "SELECT l FROM LocalStudent l WHERE l.promo.promoId = :promoId ORDER BY l.userId",
                        LocalStudent.class)
                .setParameter("promoId", promoId)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<LocalStudent> findByLastName(int pageNumber, int pageSize, String lastName) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Pagination invalide");
        }
        return em.createQuery(
                        "SELECT u FROM LocalStudent u WHERE UPPER(u.lastName) = UPPER(:lastName) ORDER BY u.userId",
                        LocalStudent.class)
                .setParameter("lastName", lastName)
                .setFirstResult(pageNumber*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public LocalStudent findByEmailUniv(String emailUniv) {
        return em.createQuery(
                        "SELECT u FROM LocalStudent u WHERE LOWER(u.emailUniv) = LOWER(:emailUniv) ORDER BY u.userId",
                        LocalStudent.class)
                .setParameter("emailUniv", emailUniv)
                .getSingleResult();
    }

    @Override
    public List<LocalStudent> findAll(int pageNumber, int pageSize) {
        //Utiliser des named queries ou la criteria API
        String jpql = "SELECT e FROM LocalStudent e ORDER BY e.userId";
        return em.createQuery(jpql, LocalStudent.class)
                .setFirstResult(pageNumber*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
