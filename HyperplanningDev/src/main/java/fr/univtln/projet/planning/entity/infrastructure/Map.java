package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.infrastructure.Building;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Map {

    private String city;
    private String imageFileName; // peut être stocker l'image directement

    private final Set<Building> buildings = new HashSet<>();

    // Factory

    private Map(String city, String imageFileName) {
        this.city = city;
        this.imageFileName = imageFileName;
    }

    public static Map MapFactory(String city, String imageFileName) {
        return new Map(city, imageFileName);
    }

    // getter setter
    public String getCity() {
        return city;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Set<Building> getBuildings() {
        return buildings;
    }

    //manage Building

    public void addBuilding(Building b) {
        if (b == null){
            return; //throw ?
        }
        else {
            buildings.add(b);
            b.setMap(this);
        }
    }

    public void removeBuilding(Building b) {
        if (b == null) {
            return; //throw
        }
        else {
            buildings.remove(b);
            b.setMap(null);
        }
    }

}
