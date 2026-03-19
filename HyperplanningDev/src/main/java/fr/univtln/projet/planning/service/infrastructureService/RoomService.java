package fr.univtln.projet.planning.service.infrastructureService;

import java.util.List;
import java.util.stream.Collectors;             // Modèle JPA (BDD)

import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;       // Modèle Métier (DTO)
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.repository.infrastructureRepository.RoomRepository;

public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // --- MÉTHODES MÉTIER ---

    public RoomEntity createRoom(RoomEntity domainRoom) {
        Room jpaRoom = toJpaModel(domainRoom);
        Room savedRoom = roomRepository.save(jpaRoom);
        return toDomainEntity(savedRoom);
    }

    public RoomEntity getRoomById(Long id) {
        return roomRepository.findById(id)
                .map(this::toDomainEntity)
                .orElse(null);
    }

    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    // --- MÉTHODES DE MAPPING (Traduction) ---

    /**
     * Convertit le modèle JPA (Base de données) vers l'objet Métier (RoomEntity)
     */
    private RoomEntity toDomainEntity(Room jpaRoom) {
        if (jpaRoom == null) return null;

        // Gestion de la conversion String (JPA) -> int (Métier)
        int numSalle = 0;
        try {
            if (jpaRoom.getNumber() != null) {
                numSalle = Integer.parseInt(jpaRoom.getNumber());
            }
        } catch (NumberFormatException e) {
            System.err.println("Attention: Le numéro de salle JPA n'est pas un entier valide : " + jpaRoom.getNumber());
        }

        RoomEntity domainRoom = RoomEntity.RoomFactory(
            numSalle, 
            jpaRoom.getCapacity(), 
            jpaRoom.getType(), 
            null // Placeholder : TODO Gérer la conversion de jpaRoom.getBuilding() vers BuildingEntity
        );

        // TODO: Boucler sur jpaRoom.getPlanning() pour convertir et ajouter les CourseEntity à domainRoom

        return domainRoom;
    }

    /**
     * Convertit l'objet Métier (RoomEntity) vers le modèle JPA (Base de données)
     */
    private Room toJpaModel(RoomEntity domainRoom) {
        if (domainRoom == null) return null;

        // Gestion de la conversion int (Métier) -> String (JPA)
        String stringNumber = String.valueOf(domainRoom.getNum());

        Room jpaRoom = Room.RoomFactory(
            stringNumber, 
            domainRoom.getCapacity(), 
            domainRoom.getType(), 
            null // Placeholder : TODO Gérer la conversion de domainRoom.getBuilding() vers Building JPA
        );

        // TODO: Boucler sur domainRoom.getCourses() pour convertir et ajouter les Course JPA à jpaRoom

        return jpaRoom;
    }
}