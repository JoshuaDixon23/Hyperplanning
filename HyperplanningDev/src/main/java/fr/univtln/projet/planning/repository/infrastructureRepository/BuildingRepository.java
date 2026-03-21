package fr.univtln.projet.planning.repository.infrastructureRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class BuildingRepository extends JpaRepository<Building, Long> {

    public BuildingRepository(EntityManager entityManager) {
        super(Building.class, entityManager);
    }

    public List<Building> findAll() {
        String jpql = "SELECT b FROM Building b";
        TypedQuery<Building> query = em.createQuery(jpql, Building.class);
        return query.getResultList();
    }

    @Override
    public Optional<Building> findById(Long campusId) {
        String jpql = "SELECT b FROM Building b WHERE b.campus.idCampus = :campusId";
        TypedQuery<Building> query = em.createQuery(jpql, Building.class);
        query.setParameter("campusId", campusId);
        List<Building> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<Building> findByName(String buildingName) {
        String jpql = "SELECT b FROM Building b WHERE b.name = :buildingName";
        TypedQuery<Building> query = em.createQuery(jpql, Building.class);
        query.setParameter("buildingName", buildingName);
        List<Building> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}