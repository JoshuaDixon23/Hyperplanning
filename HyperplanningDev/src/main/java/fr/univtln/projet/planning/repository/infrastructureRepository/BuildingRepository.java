package fr.univtln.projet.planning.repository.infrastructureRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.infrastructure.Building;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class BuildingRepository {

    private final EntityManager entityManager;

    public BuildingRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Building save(Building building) {
        entityManager.getTransaction().begin();
        if (building.getIdBuilding() == null) {
            entityManager.persist(building); // Nouveau
        } else {
            building = entityManager.merge(building); // Mise à jour
        }
        entityManager.getTransaction().commit();
        return building;
    }

    public Optional<Building> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Building.class, id));
    }

    public List<Building> findAll() {
        String jpql = "SELECT b FROM Building b";
        TypedQuery<Building> query = entityManager.createQuery(jpql, Building.class);
        return query.getResultList();
    }

    // Exemple : Trouver les bâtiments d'un campus spécifique
    public List<Building> findByCampusId(Long campusId) {
        String jpql = "SELECT b FROM Building b WHERE b.campus.idCampus = :campusId";
        TypedQuery<Building> query = entityManager.createQuery(jpql, Building.class);
        query.setParameter("campusId", campusId);
        return query.getResultList();
    }
}