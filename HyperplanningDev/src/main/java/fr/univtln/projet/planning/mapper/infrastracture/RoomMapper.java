package fr.univtln.projet.planning.mapper.infrastracture;

import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.modele.infrastructure.Room;

public class RoomMapper {

    private RoomMapper() {
        // Prevent instantiation of utility class
    }

    public static RoomEntity toDomain(Room r) {
        if (r == null) return null;
        return RoomEntity.RoomFactory(r.getNumber(), r.getCapacity(), r.getType(),
                BuildingMapper.toDomain(r.getBuilding()));
    }

    public static Room toJpa(RoomEntity r) {
        if (r == null) return null;
        return Room.RoomFactory(r.getNumber(), r.getCapacity(), r.getType(),
                BuildingMapper.toJpa(r.getBuilding()));
    }
}
