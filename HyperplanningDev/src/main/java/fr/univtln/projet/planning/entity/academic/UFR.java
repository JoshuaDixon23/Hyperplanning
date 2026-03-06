package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.infrastructure.Building;
import fr.univtln.projet.planning.entity.person.Admin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UFR {
    private String name;
    private String campus;
    private final Admin admin;

    // private Admin admin:

    private final Set<Promo> promos = new HashSet<>();
    private final Set<Building> buildings = new HashSet<>();


    // factory
    private UFR(String name, String campus,Admin admin) {
        this.name = name;
        this.campus = campus;
        this.admin=admin;
    }

    public static UFR UFRFactory(String name, String campus,Admin admin) {
        return new UFR(name, campus,admin);
    }

    // getter setter

    public String getName() {
        return name;
    }

    public String getCampus() {
        return campus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public Admin getAdmin() {
        return admin;
    }

    public Set<Promo> getModules() {
        return promos;
    }

    public Set<Building> getBuildings() {
        return buildings;
    }


    // manage ufr

    public void addPromo(Promo p) {
        if (p == null){
            return; //throw ?
        }
        else {
            promos.add(p);
            p.setUfr(this); // set ufr de module à faire
        }
    }

    public void removePromo(Promo p) {
        if (p == null) {
            return; //throw
        }
        else{
            promos.remove(p);
            p.setUfr(null);
        }
    }

    public void addBuilding(Building b) {
        if (b == null) {
            return;
        }
        else{
            buildings.add(b);
            b.setUfr(this); // set ufr à faire
        }
    }

    public void removeBuilding(Building b) {
        if (b == null) {
            return;
        }
        else{
            buildings.remove(b);
            b.setUfr(null); // set ufr à faire
        }
    }


}
