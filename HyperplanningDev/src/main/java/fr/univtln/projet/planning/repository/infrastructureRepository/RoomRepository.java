package fr.univtln.projet.planning.repository.infrastructureRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.infrastructure.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class RoomRepository {

    private final EntityManager entityManager;

    public RoomRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Room save(Room room) {
        entityManager.getTransaction().begin();
        if (room.getIdRoom() == null) {
            entityManager.persist(room); // Nouvelle salle
        } else {
            room = entityManager.merge(room); // Mise à jour
        }
        entityManager.getTransaction().commit();
        return room;
    }

    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Room.class, id));
    }

    public List<Room> findAll() {
        String jpql = "SELECT r FROM Room r";
        TypedQuery<Room> query = entityManager.createQuery(jpql, Room.class);
        return query.getResultList();
    }

    // Exemple utile : Trouver toutes les salles d'un bâtiment donné
    public List<Room> findByBuildingId(Long idBuilding) {
        String jpql = "SELECT r FROM Room r WHERE r.building.idBuilding = :idBuilding";
        TypedQuery<Room> query = entityManager.createQuery(jpql, Room.class);
        query.setParameter("idBuilding", idBuilding);
        return query.getResultList();
    }
}