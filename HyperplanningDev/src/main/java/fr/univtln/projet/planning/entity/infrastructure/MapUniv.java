package fr.univtln.projet.planning.entity.infrastructure;

import java.util.HashSet;
import java.util.Set;

public class MapUniv {

    private String city;
    private String imageFileName; // peut être stocker l'image directement

    private final Set<Building> buildings = new HashSet<>();

    // Factory

    private MapUniv(String city, String imageFileName) {
        this.city = city;
        this.imageFileName = imageFileName;
    }

    public static MapUniv MapFactory(String city, String imageFileName) {
        return new MapUniv(city, imageFileName);
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
            b.setMapUniv(this);
        }
    }

    public void removeBuilding(Building b) {
        if (b == null) {
            return; //throw
        }
        else {
            buildings.remove(b);
            b.setMapUniv(null);
        }
    }



}
