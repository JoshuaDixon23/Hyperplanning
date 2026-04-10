package fr.univtln.projet.planning.service.infrastructureService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.mapper.infrastracture.RoomMapper;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.modele.infrastructure.RoomType;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.repository.infrastructureRepository.RoomRepository;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;

import static fr.univtln.projet.planning.entity.infrastructure.RoomEntity.buildRoomKey;

public class RoomService {

    private final RoomRepository roomRepository;
    private final BuildingService buildingService;
    private final CourseRepository courseRepository;

    public RoomService(RoomRepository roomRepository,
                       BuildingService buildingService,
                        CourseRepository courseRepository) {
        this.roomRepository = roomRepository;
        this.buildingService = buildingService;
        this.courseRepository= courseRepository;
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

    public Room findByNumber(String number) {
        return roomRepository.findByNumber(number);
    }

    public List<RoomEntity> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(RoomMapper::toDomain)
                .toList();
    }


    public List<RoomEntity> findAvailableRooms(LocalDate date, LocalTime start, LocalTime end) {
        if (date == null || start == null || end == null) {
            throw new IllegalArgumentException("Date ou heures nulles");
        }

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Plage horaire invalide");
        }

        List<RoomEntity> allRooms = findAll();
        List<Course> coursesOfDay = courseRepository.findByDate(date);

        Set<String> occupiedRoomKeys = coursesOfDay.stream()
                .filter(course -> course.getRoom() != null)
                .filter(course -> overlaps(course, start, end))
                .map(course -> buildRoomKey(course.getRoom().getBuilding().getName(), course.getRoom().getNumber()))
                .collect(Collectors.toSet());

        return allRooms.stream()
                .filter(room -> !occupiedRoomKeys.contains(
                        buildRoomKey(room.getBuilding().getName(), room.getNumber())
                ))
                .toList();
    }



    private boolean overlaps(Course course, LocalTime start, LocalTime end) {
        LocalTime courseStart = course.getStartTime();
        LocalTime courseEnd = courseStart.plus(course.getDuration());

        return courseStart.isBefore(end) && courseEnd.isAfter(start);
    }


}