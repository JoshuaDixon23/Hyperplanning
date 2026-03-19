package fr.univtln.projet.planning.entity.infrastructure;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.univtln.projet.planning.entity.academic.UFREntity;
import fr.univtln.projet.planning.modele.infrastructure.Day;
import fr.univtln.projet.planning.modele.infrastructure.RoomType;

public class BuildingEntity {

    public static class Hours{
        public LocalTime opening;
        public LocalTime closing;
    }

    private String name;
    private String localisation;
    private Map<Day,Hours> openingHours;

    //private LocalTime openingTime; // à modifier regarder diagramme
    //private LocalTime closingTime;

    private UFREntity ufr; //peut être null
    private CampusEntity campus;
    private final Set<RoomEntity> rooms = new HashSet<>();


    //factory
    private BuildingEntity(String name, String localisation, Map<Day,Hours> openingHours) {
        this.localisation = localisation;
        this.name = name;
        this.openingHours = openingHours;
    }

    public static BuildingEntity BuildingFactory(String name, String localisation, Map openingHours) {
        return new BuildingEntity(name, localisation,openingHours);
    }

    // getter setter
    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Map<Day,Hours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Map<Day,Hours> openingHours) {
        this.openingHours = openingHours;
    }

    public UFREntity getUfr(){
        return ufr;
    }

    public void setUfr(UFREntity u){
        this.ufr=u;
    }

    public CampusEntity getCampus() {
        return campus;
    }

    public void setCampus(CampusEntity campus) {
        this.campus = campus;
    }

    public Set<RoomEntity> getRooms() {
        return Collections.unmodifiableSet(rooms);
    }

    public String getName() {
        return name;
    }

// manage Building

    public void addRoom(int num, int capacity, RoomType type) {
        /*if (r == null) {
            return; //throw ?
        }

         */
        RoomEntity r = RoomEntity.RoomFactory(num,capacity,type,this);
        rooms.add(r);

    }

    public void removeRoom(RoomEntity r) {
        if (r == null) {
            return; //throw ?
        }
        else {
            rooms.remove(r);
        }
    }


    // equals hashCode


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BuildingEntity building = (BuildingEntity) o;
        return Objects.equals(name, building.name) && Objects.equals(localisation, building.localisation) && Objects.equals(ufr, building.ufr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, localisation, ufr);
    }
}
