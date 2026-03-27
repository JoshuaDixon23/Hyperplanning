package fr.univtln.projet.planning.service.infrastructureService;

import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.modele.infrastructure.RoomType;
import fr.univtln.projet.planning.repository.infrastructureRepository.RoomRepository;

import java.util.List;

public class RoomService {

    private final RoomRepository roomRepository;
    private final BuildingService buildingService;

    public RoomService(RoomRepository roomRepository,
                       BuildingService buildingService) {
        this.roomRepository = roomRepository;
        this.buildingService = buildingService;
    }

    // ------------------ MAPPERS ------------------

    public RoomEntity toDomain(Room r) {
        if (r == null) return null;
        return RoomEntity.RoomFactory(r.getNumber(), r.getCapacity(), r.getType(),
                buildingService.toDomain(r.getBuilding()));
    }

    public Room toJpa(RoomEntity r) {
        if (r == null) return null;
        return Room.RoomFactory(r.getNumber(), r.getCapacity(), r.getType(),
                buildingService.toJpa(r.getBuilding()));
    }

    // ------------------ CREATE ------------------

    public RoomEntity create(RoomEntity entity) {
        Room saved = roomRepository.save(toJpa(entity));
        return toDomain(saved);
    }

    public RoomEntity create(String num, int capacity, RoomType type, String buildingName) {
        Building building = buildingService.findJpaByName(buildingName);
        if (building == null) {
            throw new IllegalArgumentException("Building not found: " + buildingName);
        }
        Room room = Room.RoomFactory(num, capacity, type, building);
        Room saved = roomRepository.save(room);
        return toDomain(saved);
    }

    // ------------------ FIND ------------------

    public RoomEntity findById(Long id) {
        return roomRepository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    public List<RoomEntity> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }
}