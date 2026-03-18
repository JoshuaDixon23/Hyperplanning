package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.infrastructure.BuildingEntity;
import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;
import fr.univtln.projet.planning.entity.person.AdminEntity;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.modele.infrastructure.Campus;

import java.util.HashSet;
import java.util.Set;

public class UFREntity {
    private String name;
    private AdminEntity admin;
    private CampusEntity campus;

    // private Admin admin:
    private final Set<PromoEntity> promos = new HashSet<>();
    private final Set<BuildingEntity> buildings = new HashSet<>();


    // factory
    private UFREntity(String name, CampusEntity campus,AdminEntity admin) {
        this.name = name;
        this.admin=admin;
        this.campus=campus;
    }

    public static UFREntity UFRFactory(String name, CampusEntity campus,AdminEntity admin) {
        return new UFREntity(name, campus,admin);
    }

    // getter setter

    public String getName() {
        return name;
    }

    public CampusEntity getCampus() {
        return campus;
    }

    public void setCampus(CampusEntity campus) {
        this.campus = campus;
    }

    public void setName(String name) {
        this.name = name;
    }


    public AdminEntity getAdmin() {
        return admin;
    }

    public Set<PromoEntity> getModules() {
        return promos;
    }

    public Set<BuildingEntity> getBuildings() {
        return buildings;
    }



    // manage ufr


    // create promo

    public void addPromo(String promoName, StudyLevel promoStudyLevel) {
        if (promoName == null || promoStudyLevel == null){
            return; //throw ?
        }
        else {
            promos.add(PromoEntity.PromoFactory(promoName, promoStudyLevel, this));
        }
    }

    public void removePromo(PromoEntity p) {
        if (p == null) {
            return; //throw
        }
        else{
            promos.remove(p);
            //p.setUfr(null);
        }
    }

    public void addBuilding(BuildingEntity b) {
        if (b == null) {
            return;
        }
        else{
            buildings.add(b);
            b.setUfr(this); // set ufr à faire
        }
    }

    public void removeBuilding(BuildingEntity b) {
        if (b == null) {
            return;
        }
        else{
            buildings.remove(b);
            b.setUfr(null); // set ufr à faire
        }
    }



}
