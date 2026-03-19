package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.academic.UFR;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class UFRRepository {

    private final EntityManager entityManager;

    public UFRRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Sauvegarder ou mettre à jour une UFR
    public UFR save(UFR ufr) {
        entityManager.getTransaction().begin();
        if (ufr.getIdUFR() == null) {
            entityManager.persist(ufr); // Nouvelle UFR
        } else {
            ufr = entityManager.merge(ufr); // Mise à jour
        }
        entityManager.getTransaction().commit();
        return ufr;
    }

    // Trouver par ID
    public Optional<UFR> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UFR.class, id));
    }

    // Récupérer toutes les UFRs
    public List<UFR> findAll() {
        String jpql = "SELECT u FROM UFR u";
        TypedQuery<UFR> query = entityManager.createQuery(jpql, UFR.class);
        return query.getResultList();
    }

    // Trouver une UFR par son nom
    public Optional<UFR> findByName(String name) {
        String jpql = "SELECT u FROM UFR u WHERE u.name = :name";
        TypedQuery<UFR> query = entityManager.createQuery(jpql, UFR.class);
        query.setParameter("name", name);
        
        List<UFR> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}