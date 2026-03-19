package fr.univtln.projet.planning.repository.infrastructureRepository;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

public class RoomRepository extends JpaRepository<Room, Long > {
    protected RoomRepository(Class<Room> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }
}
