package fr.univtln.projet.planning.repository.infrastructureRepository;

import fr.univtln.projet.planning.modele.infrastructure.Campus;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

public class CampusRepository extends JpaRepository<Campus, Long > {
    protected CampusRepository(EntityManager entityManager) {
        super(Campus.class, entityManager);
    }
}
