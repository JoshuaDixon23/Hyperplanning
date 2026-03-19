package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class UFRRepository extends JpaRepository<UFR, Long> {

    public UFRRepository(EntityManager entityManager) {
        super(UFR.class, entityManager);
    }

    public List<UFR> findAll() {
        String jpql = "SELECT u FROM UFR u";
        TypedQuery<UFR> query = em.createQuery(jpql, UFR.class);
        return query.getResultList();
    }

    public Optional<UFR> findByName(String name) {
        String jpql = "SELECT u FROM UFR u WHERE u.name = :name";
        TypedQuery<UFR> query = em.createQuery(jpql, UFR.class);
        query.setParameter("name", name);
        
        List<UFR> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}