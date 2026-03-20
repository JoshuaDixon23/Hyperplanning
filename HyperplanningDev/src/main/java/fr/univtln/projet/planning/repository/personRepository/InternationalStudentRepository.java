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
}
