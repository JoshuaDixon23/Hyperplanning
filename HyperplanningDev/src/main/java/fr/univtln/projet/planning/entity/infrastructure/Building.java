package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.academic.UFR;

import java.time.LocalTime; // si ok ?
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Building {

    private String localisation;
    private LocalTime openingTime; // à modifier regardier diagramme
    private LocalTime closingTime;

    private UFR ufr;
    private Map map;
    private final Set<Room> rooms = new HashSet<>();


    //fatcory
    private Building(String localisation, LocalTime openingTime, LocalTime closingTime) {
        this.localisation = localisation;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public static Building BuildingFactory(String localisation, LocalTime openingTime, LocalTime closingTime) {
        return new Building(localisation, openingTime, closingTime);
    }

    // getter setter
    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public UFR getUfr(){
        return ufr;
    }

    public void setUfr(UFR u){
        this.ufr=u;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
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
}
