package fr.univtln.projet.planning.service.infrastructureService;

import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.mapper.infrastracture.RoomMapper;
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

    // ------------------ CREATE ------------------

    public RoomEntity create(RoomEntity entity) {
        Room saved = roomRepository.save(RoomMapper.toJpa(entity));
        return RoomMapper.toDomain(saved);
    }


    public RoomEntity create(String num, int capacity, RoomType type, String buildingName) {

        Building building = buildingService.findJpaByName(buildingName);
        if (building == null) {
            throw new IllegalArgumentException("Building not found: " + buildingName);
        }
        Room room = Room.RoomFactory(num, capacity, type, building);
        Room saved = roomRepository.save(room);
        return RoomMapper.toDomain(saved);
    }

    // ------------------ FIND ------------------

    public RoomEntity findById(Long id) {
        return roomRepository.findById(id)
                .map(RoomMapper::toDomain)
                .orElse(null);
    }

    public List<RoomEntity> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(RoomMapper::toDomain)
                .toList();
    }
}