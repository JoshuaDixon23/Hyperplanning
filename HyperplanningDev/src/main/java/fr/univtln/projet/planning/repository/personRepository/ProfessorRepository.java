package fr.univtln.projet.planning.repository.personRepository;

import java.util.List;

import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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
        String jpql = "SELECT u.userId FROM User u WHERE u.emailUniv = :emailUniv";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("emailUniv", emailUniv);
        
        Long userId = query.getSingleResult();
        
        Professor prof = em.find(Professor.class, userId);
        
        if (prof == null) {
            throw new jakarta.persistence.NoResultException(
                "L'email " + emailUniv + " appartient à un User, mais cet utilisateur n'est pas enregistré dans la table Professors !"
            );
        }
        
        return prof;
    }
}
