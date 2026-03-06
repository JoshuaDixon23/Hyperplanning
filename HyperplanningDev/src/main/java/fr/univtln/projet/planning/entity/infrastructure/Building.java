package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.academic.UFR;

import java.time.LocalTime; // si ok ?
import java.util.*;

public class Building {

    private String name;
    private String localisation;
    private Map openingHours;

    //private LocalTime openingTime; // à modifier regardier diagramme
    //private LocalTime closingTime;

    private UFR ufr;
    private MapUniv mapUniv;
    private final Set<Room> rooms = new HashSet<>();


    //fatcory
    private Building(String name, String localisation, Map openingHours) {
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

    public void setOpeningHours(Map openingHours) {
        this.openingHours = openingHours;
    }

    public UFR getUfr(){
        return ufr;
    }

    public void setUfr(UFR u){
        this.ufr=u;
    }

    public MapUniv getMapUniv() {
        return mapUniv;
    }

    public void setMapUniv(MapUniv mapUniv) {
        this.mapUniv = mapUniv;
    }

    public Set<Room> getRooms() {
        return Collections.unmodifiableSet(rooms);
    }

    // manage Building

    public void addRoom(Room r) {
        if (r == null) {
            return; //throw ?
        }
        else {
            rooms.add(r);
            r.setBuilding(this); // Room appartient à Building
        }
    }

    public void removeRoom(Room r) {
        if (r == null) {
            return; //throw ?
        }
        else {
            rooms.remove(r);
            r.setBuilding(null); // Room appartient à Building
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
