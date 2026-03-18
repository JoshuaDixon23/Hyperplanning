package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class LocalStudentRepository extends JpaRepository<LocalStudent, Integer> {

    protected LocalStudentRepository(EntityManager entityManager) {
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
}
