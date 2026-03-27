package fr.univtln.projet.planning.repository.personRepository;


import fr.univtln.projet.planning.modele.person.Student;

import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class StudentRepository extends JpaRepository<Student, Long> {

    public StudentRepository(EntityManager entityManager) {
        super(Student.class, entityManager);
    }

    public List<Student> findByLastName(int pageNumber, int pageSize, String lastName) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Pagination invalide");
        }
        return em.createQuery(
                        "SELECT u FROM User u WHERE UPPER(u.lastName) = UPPER(:lastName) ORDER BY u.userId",
                        Student.class)
                .setParameter("lastName", lastName)
                .setFirstResult(pageNumber*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

}
