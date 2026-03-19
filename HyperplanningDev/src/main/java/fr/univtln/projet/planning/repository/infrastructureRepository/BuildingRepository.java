package fr.univtln.projet.planning.repository.infrastructureRepository;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

public class BuildingRepository extends JpaRepository<Building, Long > {
    protected BuildingRepository(Class<Building> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }
}
