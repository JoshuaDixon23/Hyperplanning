package fr.univtln.projet.planning.repository.infrastructureRepository;

import java.util.List;

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

    public List<Building> findByCampusId(Long campusId) {
        String jpql = "SELECT b FROM Building b WHERE b.campus.idCampus = :campusId";
        TypedQuery<Building> query = em.createQuery(jpql, Building.class);
        query.setParameter("campusId", campusId);
        return query.getResultList();
    }
}