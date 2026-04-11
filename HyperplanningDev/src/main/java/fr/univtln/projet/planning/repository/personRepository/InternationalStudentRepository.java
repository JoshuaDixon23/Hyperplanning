package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.InternationalStudent;

import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class InternationalStudentRepository extends JpaRepository<InternationalStudent, Long> {

    public InternationalStudentRepository(EntityManager entityManager) {
        super(InternationalStudent.class, entityManager);
    }

    public List<InternationalStudent> findByLastName(int pageNumber, int pageSize, String lastName) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Pagination invalide");
        }
        return em.createQuery(
                        "SELECT u FROM User u WHERE UPPER(u.lastName) = UPPER(:lastName) ORDER BY u.userId",
                        InternationalStudent.class)
                .setParameter("lastName", lastName)
                .setFirstResult(pageNumber*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<InternationalStudent> findAll(int pageNumber, int pageSize) {
        //Utiliser des named queries ou la criteria API
        String jpql = "SELECT e FROM InternationalStudent e ORDER BY e.userId DESC";
        return em.createQuery(jpql, InternationalStudent.class)
                .setFirstResult(pageNumber*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public InternationalStudent findByEmailUniv(String emailUniv) {
        return em.createQuery(
                        "SELECT u FROM InternationalStudent u WHERE LOWER(u.emailUniv) = LOWER(:emailUniv) ORDER BY u.userId",
                        InternationalStudent.class)
                .setParameter("emailUniv", emailUniv)
                .getSingleResult();
    }
}
