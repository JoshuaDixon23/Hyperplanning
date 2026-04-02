package fr.univtln.projet.planning.repository.infrastructureRepository;

import java.util.List;

import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class RoomRepository extends JpaRepository<Room, Long> {

    public RoomRepository(EntityManager entityManager) {
        super(Room.class, entityManager);
    }

    public List<Room> findAll() {
        String jpql = "SELECT r FROM Room r";
        TypedQuery<Room> query = em.createQuery(jpql, Room.class);
        return query.getResultList();
    }

    public List<Room> findByBuildingId(Long idBuilding) {
        String jpql = "SELECT r FROM Room r WHERE r.building.idBuilding = :idBuilding";
        TypedQuery<Room> query = em.createQuery(jpql, Room.class);
        query.setParameter("idBuilding", idBuilding);
        return query.getResultList();
    }

    public Room findByNumber(String number) {
        String jpql = "SELECT r FROM Room r WHERE r.number = :number";
        TypedQuery<Room> query = em.createQuery(jpql, Room.class);
        query.setParameter("number", number);
        return query.getSingleResult();
    }
}