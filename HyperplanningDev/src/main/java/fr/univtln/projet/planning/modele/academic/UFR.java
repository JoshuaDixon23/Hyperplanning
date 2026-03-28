package fr.univtln.projet.planning.modele.academic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fr.univtln.projet.planning.modele.infrastructure.Building;
import fr.univtln.projet.planning.modele.infrastructure.Campus;
import fr.univtln.projet.planning.modele.person.Admin;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "UFR")
@Getter
@Setter
public class UFR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUFR;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "idCampus", nullable = false)
    private Campus campus;

    @OneToMany(mappedBy = "ufr", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Promo> promos = new HashSet<>();


    @OneToMany(mappedBy = "ufr")
    private Set<Building> buildings = new HashSet<>();

    protected UFR() {
    }

    private UFR(String name, Campus campus, Admin admin) {
        this.name = name;
        this.campus = campus;
        this.admin = admin;
    }


    public static UFR UFRFactory(String name, Campus campus, Admin admin) {
        Objects.requireNonNull(name, "UFR name cannot be null");
        Objects.requireNonNull(campus, "UFR must be linked to a Campus");
        // Admin can potentially be null if not yet assigned
        
        return new UFR(name, campus, admin);
    }


    public Set<Promo> getPromos() {
        return Collections.unmodifiableSet(promos);
    }


    public void addPromo(String promoName, int year, StudyLevel promoStudyLevel) {
        if (promoName != null && promoStudyLevel != null) {
            Promo newPromo = Promo.PromoFactory(promoName, year, promoStudyLevel, this);
            promos.add(newPromo);
            // newPromo.setUfr(this) is handled inside your PromoFactory
        }
    }

    public void addPromo(Promo promo) {
        if (promo != null) {
            promos.add(promo);
            promo.setUfr(this);
        }
    }

    public void removePromo(Promo p) {
        if (p != null) {
            promos.remove(p);
            p.setUfr(null);
        }
    }

    public Set<Building> getBuildings() {
        return Collections.unmodifiableSet(buildings);
    }

    public void addBuilding(Building b) {
        if (b != null) {
            buildings.add(b);
            b.setUfr(this); 
        }
    }

    public void removeBuilding(Building b) {
        if (b != null) {
            buildings.remove(b);
            b.setUfr(null); 
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UFR ufr = (UFR) o;
        // UFRs are usually unique by name within a Campus
        return Objects.equals(name, ufr.name) && Objects.equals(campus, ufr.campus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, campus);
    }
}