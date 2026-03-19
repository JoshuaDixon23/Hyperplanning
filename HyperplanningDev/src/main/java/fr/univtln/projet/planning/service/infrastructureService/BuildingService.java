package fr.univtln.projet.planning.service.infrastructureService;

import java.util.EnumMap;
import java.util.List;             // Modèle JPA
import java.util.Map;       // Modèle Métier
import java.util.stream.Collectors;

import fr.univtln.projet.planning.entity.infrastructure.BuildingEntity;
import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.modele.infrastructure.Day;
import fr.univtln.projet.planning.repository.infrastructureRepository.BuildingRepository;
import fr.univtln.projet.planning.service.academicService.UFRService;

public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final CampusService campusService;
    private final UFRService ufrService;
    private final RoomService roomService;

    public BuildingService(BuildingRepository buildingRepository, CampusService campusService, UFRService ufrService, RoomService roomService) {
        this.buildingRepository = buildingRepository;
        this.campusService = campusService;
        this.ufrService = ufrService;
        this.roomService = roomService;
    }

    // --- MÉTHODES MÉTIER ---

    public BuildingEntity createBuilding(BuildingEntity domainBuilding) {
        Building jpaBuilding = toJpaModel(domainBuilding);
        Building savedBuilding = buildingRepository.save(jpaBuilding);
        return toDomainEntity(savedBuilding);
    }

    public BuildingEntity getBuildingById(Long id) {
        return buildingRepository.findById(id)
                .map(this::toDomainEntity)
                .orElse(null);
    }

    public List<BuildingEntity> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    // --- MÉTHODES DE MAPPING (Traduction) ---

    private BuildingEntity toDomainEntity(Building jpaBuilding) {
        if (jpaBuilding == null) return null;

        Map<Day, BuildingEntity.Hours> domainHoursMap = new EnumMap<>(Day.class);
        
        if (jpaBuilding.getOpeningHours() != null) {
            for (Map.Entry<Day, Building.Hours> entry : jpaBuilding.getOpeningHours().entrySet()) {
                BuildingEntity.Hours domainHours = new BuildingEntity.Hours(); 
                domainHours.opening = entry.getValue().getOpening();
                domainHours.closing = entry.getValue().getClosing();
                domainHoursMap.put(entry.getKey(), domainHours);
            }
        }

        BuildingEntity domainBuilding = BuildingEntity.BuildingFactory(jpaBuilding.getName(), jpaBuilding.getLocalisation(), domainHoursMap);

        domainBuilding.setCampus(campusService.toDomainEntity(jpaBuilding.getCampus()));
        domainBuilding.setUfr(ufrService.toDomainEntity(jpaBuilding.getUfr()));
        
        if (jpaBuilding.getRooms() != null) {
            jpaBuilding.getRooms().forEach(r -> {
                // Ta RoomEntity se crée via la BuildingEntity existante
                domainBuilding.addRoom(Integer.parseInt(r.getNumber()), r.getCapacity(), r.getType());
            });
        }
        return domainBuilding;
    }

    private Building toJpaModel(BuildingEntity domainBuilding) {
        if (domainBuilding == null) return null;

        // 1. Traduction de la Map des horaires (Métier -> JPA)
        Map<Day, Building.Hours> jpaHoursMap = new EnumMap<>(Day.class);
        
        if (domainBuilding.getOpeningHours() != null) {
            // On cast pour s'assurer du type car ton getter renvoie une Map brute pour l'instant
            Map<Day, BuildingEntity.Hours> domainHoursMap = domainBuilding.getOpeningHours();
            
            for (Map.Entry<Day, BuildingEntity.Hours> entry : domainHoursMap.entrySet()) {
                Building.Hours jpaHours = new Building.Hours(
                    entry.getValue().opening, 
                    entry.getValue().closing
                );
                jpaHoursMap.put(entry.getKey(), jpaHours);
            }
        }

        // 2. Création de l'objet JPA
        Building jpaBuilding = Building.BuildingFactory(
            domainBuilding.getName(), 
            domainBuilding.getLocalisation(), 
            jpaHoursMap
        );

        jpaBuilding.setCampus(campusService.toJpaModel(domainBuilding.getCampus()));
        jpaBuilding.setUfr(ufrService.toJpaModel(domainBuilding.getUfr()));

        if (domainBuilding.getRooms() != null) {
             domainBuilding.getRooms().forEach(r -> jpaBuilding.addRoom(roomService.toJpaModel(r)));
        }
        return jpaBuilding;
    }
}