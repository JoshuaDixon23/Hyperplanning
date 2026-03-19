package fr.univtln.projet.planning.repository.infrastructureRepository;

import java.util.List;
import java.util.Optional;

public class CampusRepository extends JpaRepository<Campus, Long > {
    protected CampusRepository(EntityManager entityManager) {
        super(Campus.class, entityManager);
    }

    // Sauvegarder ou mettre à jour un campus
    public Campus save(Campus campus) {
        entityManager.getTransaction().begin();
        if (campus.getIdCampus() == null) {
            entityManager.persist(campus); // Nouveau
        } else {
            campus = entityManager.merge(campus); // Mise à jour
        }
        entityManager.getTransaction().commit();
        return campus;
    }

    // Trouver par ID
    public Optional<Campus> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Campus.class, id));
    }

    // Récupérer tous les campus
    public List<Campus> findAll() {
        String jpql = "SELECT c FROM Campus c";
        TypedQuery<Campus> query = entityManager.createQuery(jpql, Campus.class);
        return query.getResultList();
    }

    // Trouver un campus par sa ville
    public Optional<Campus> findByCity(String city) {
        String jpql = "SELECT c FROM Campus c WHERE c.city = :city";
        TypedQuery<Campus> query = entityManager.createQuery(jpql, Campus.class);
        query.setParameter("city", city);
        
        List<Campus> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}