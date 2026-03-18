package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.modele.person.Student;

import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

public class StudentRepository extends JpaRepository<Student, Long> {

    protected StudentRepository(EntityManager entityManager) {
        super(Student.class, entityManager);
    }

}
