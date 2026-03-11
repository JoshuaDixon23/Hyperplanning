package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.academic.UFR;

import java.util.HashSet;
import java.util.Set;

public class Campus {

    private String city;
    private String imageFileName; // peut être stocker l'image directement

    private final Set<Building> buildings = new HashSet<>();
    private final Set<UFR> ufrs = new HashSet<>();

    // Factory

    private Campus(String city, String imageFileName) {
        this.city = city;
        this.imageFileName = imageFileName;
    }

    public static Campus CampusFactory(String city, String imageFileName) {
        return new Campus(city, imageFileName);
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
            b.setCampus(this);
        }
    }

    public void removeBuilding(Building b) {
        if (b == null) {
            return; //throw
        }
        else {
            buildings.remove(b);
            b.setCampus(null);
        }
    }

    public void addUfr(UFR u) {
        if (u == null){
            return; //throw ?
        }
        else {
            ufrs.add(u);
            u.setCampus(this);
        }
    }

    public void removeUfr(UFR u) {
        if (u == null){
            return; //throw ?
        }
        else {
            ufrs.remove(u);
            u.setCampus(null);
        }
    }





}
