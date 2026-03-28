package fr.univtln.projet.planning.entity.infrastructure;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.univtln.projet.planning.entity.academic.UFR;

public class Building {

    public class Hours{
        public LocalTime opening;
        public LocalTime closing;
    }

    private String name;
    private String localisation;
    private Map<Day,Hours> openingHours;

    //private LocalTime openingTime; // à modifier regarder diagramme
    //private LocalTime closingTime;

    private UFR ufr; //peut être null
    private Campus campus;
    private final Set<Room> rooms = new HashSet<>();


    //factory
    private Building(String name, String localisation, Map<Day,Hours> openingHours) {
        this.localisation = localisation;
        this.name = name;
        this.openingHours = openingHours;
    }

    public static Building BuildingFactory(String name, String localisation, Map openingHours) {
        return new Building(name, localisation,openingHours);
    }

    // getter setter
    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Map getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Map<Day,Hours> openingHours) {
        this.openingHours = openingHours;
    }

    public UFR getUfr(){
        return ufr;
    }

    public void setUfr(UFR u){
        this.ufr=u;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Set<Room> getRooms() {
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
        Room r = Room.RoomFactory(num,capacity,type,this);
        rooms.add(r);

    }

    public void removeRoom(Room r) {
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
        Building building = (Building) o;
        return Objects.equals(name, building.name) && Objects.equals(localisation, building.localisation) && Objects.equals(ufr, building.ufr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, localisation, ufr);
    }
}
