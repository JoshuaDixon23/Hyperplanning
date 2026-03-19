package fr.univtln.projet.planning.repository.infrastructureRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.infrastructure.Campus;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class CampusRepository extends JpaRepository<Campus, Long> {

    public CampusRepository(EntityManager entityManager) {
        super(Campus.class, entityManager);
    }

    public List<Campus> findAll() {
        String jpql = "SELECT c FROM Campus c";
        TypedQuery<Campus> query = em.createQuery(jpql, Campus.class);
        return query.getResultList();
    }

    public Optional<Campus> findByCity(String city) {
        String jpql = "SELECT c FROM Campus c WHERE c.city = :city";
        TypedQuery<Campus> query = em.createQuery(jpql, Campus.class);
        query.setParameter("city", city);
        
        List<Campus> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}