package fr.univtln.projet.planning.entity.academic;

import java.util.HashSet;
import java.util.Set;

import fr.univtln.projet.planning.entity.infrastructure.Building;
import fr.univtln.projet.planning.entity.infrastructure.Campus;
import fr.univtln.projet.planning.entity.person.Admin;

public class UFR {
    private String name;
    private Admin admin;
    private Campus campus;

    // private Admin admin:
    private final Set<Promo> promos = new HashSet<>();
    private final Set<Building> buildings = new HashSet<>();


    // factory
    private UFR(String name, Campus campus,Admin admin) {
        this.name = name;
        this.admin=admin;
        this.campus=campus;
    }

    public static UFR UFRFactory(String name, Campus campus,Admin admin) {
        return new UFR(name, campus,admin);
    }

    // getter setter

    public String getName() {
        return name;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public void setName(String name) {
        this.name = name;
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


    // create promo

    public void addPromo(String promoName, StudyLevel promoStudyLevel) {
        if (promoName == null || promoStudyLevel == null){
            return; //throw ?
        }
        else {
            promos.add(Promo.PromoFactory(promoName, promoStudyLevel, this));
        }
    }

    public void removePromo(Promo p) {
        if (p == null) {
            return; //throw
        }
        else{
            promos.remove(p);
            //p.setUfr(null);
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
