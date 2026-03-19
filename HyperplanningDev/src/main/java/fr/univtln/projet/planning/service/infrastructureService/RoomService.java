package fr.univtln.projet.planning.service.infrastructureService;

import java.util.List;
import java.util.stream.Collectors;

import fr.univtln.projet.planning.entity.infrastructure.BuildingEntity;
import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.repository.infrastructureRepository.RoomRepository;
import fr.univtln.projet.planning.service.planningService.CourseService;

public class RoomService {

    private final RoomRepository roomRepository;
    private final BuildingService buildingService;
    private final CourseService courseService;

    public RoomService(RoomRepository roomRepository, BuildingService buildingService, CourseService courseService) {
        this.roomRepository = roomRepository;
        this.buildingService = buildingService;
        this.courseService = courseService;
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

    // --- MAPPINGS ---
    private RoomEntity toDomainEntity(Room jpaRoom) {
        if (jpaRoom == null) return null;
        int numSalle = jpaRoom.getNumber() != null ? Integer.parseInt(jpaRoom.getNumber()) : 0;

        BuildingEntity domainBuilding = buildingService.toDomainEntity(jpaRoom.getBuilding());
        
        RoomEntity domainRoom = RoomEntity.RoomFactory(numSalle, jpaRoom.getCapacity(), jpaRoom.getType(), domainBuilding);

        if (jpaRoom.getPlanning() != null) {
            jpaRoom.getPlanning().forEach(c -> domainRoom.addCourse(courseService.toDomainEntity(c)));
        }
        return domainRoom;
    }

    private Room toJpaModel(RoomEntity domainRoom) {
        if (domainRoom == null) return null;

        Building jpaBuilding = buildingService.toJpaModel(domainRoom.getBuilding());

        Room jpaRoom = Room.RoomFactory(String.valueOf(domainRoom.getNum()), domainRoom.getCapacity(), domainRoom.getType(), jpaBuilding);

        if (domainRoom.getCourses() != null) {
            domainRoom.getCourses().forEach(c -> jpaRoom.addCourse(courseService.toJpaModel(c)));
        }
        return jpaRoom;
    }
}