package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.academic.UFREntity;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CampusEntity {

    // getter setter
    private final String city;
    private final String imageFileName; // peut être stocker l'image directement

    private final Set<BuildingEntity> buildings = new HashSet<>();
    private final Set<UFREntity> ufrs = new HashSet<>();

    // Factory

    private CampusEntity(String city, String imageFileName) {
        this.city = city;
        this.imageFileName = imageFileName;
    }

    public static CampusEntity CampusFactory(String city, String imageFileName) {
        return new CampusEntity(city, imageFileName);
    }

    //manage Building

    public void addBuilding(BuildingEntity b) {
        if (b == null){
            return; //throw ?
        }
        else {
            buildings.add(b);
            b.setCampus(this);
        }
    }

    public void removeBuilding(BuildingEntity b) {
        if (b == null) {
            return; //throw
        }
        else {
            buildings.remove(b);
            b.setCampus(null);
        }
    }

    public void addUfr(UFREntity u) {
        if (u == null){
            return; //throw ?
        }
        else {
            ufrs.add(u);
            u.setCampus(this);
        }
    }

    public void removeUfr(UFREntity u) {
        if (u == null){
            return; //throw ?
        }
        else {
            ufrs.remove(u);
            u.setCampus(null);
        }
    }
}
