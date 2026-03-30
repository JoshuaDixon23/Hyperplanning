package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProfessorRepository extends JpaRepository<Professor, Long> {
    public ProfessorRepository(EntityManager entityManager) {
        super(Professor.class, entityManager);
    }

    public List<Professor> findAll() {
        String jpql = "SELECT p FROM Professor p";
        TypedQuery<Professor> query = em.createQuery(jpql, Professor.class);
        return query.getResultList();
    }

    public Professor findByEmailUniv(String emailUniv) {
        String jpql = "SELECT p FROM Professor p WHERE p.emailUniv = :emailUniv";
        TypedQuery<Professor> query = em.createQuery(jpql, Professor.class);
        query.setParameter("emailUniv", emailUniv);
        return query.getSingleResult();
    }
}
