package fr.univtln.projet.planning.modele.infrastructure;

import fr.univtln.projet.planning.modele.academic.UFR; // Adjust package if needed
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Campus")
@Getter
@Setter
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCampus;

    @Column(nullable = false)
    private String city;

    private String imageFileName;


    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Building> buildings = new HashSet<>();

    // Points to the "campus" attribute in the UFR class
    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UFR> ufrs = new HashSet<>();

    protected Campus() {
    }

    private Campus(String city, String imageFileName) {
        this.city = city;
        this.imageFileName = imageFileName;
    }


    public static Campus CampusFactory(String city, String imageFileName) {
        Objects.requireNonNull(city, "City cannot be null");
        return new Campus(city, imageFileName);
    }


    public Set<Building> getBuildings() {
        return Collections.unmodifiableSet(buildings);
    }

    public void addBuilding(Building b) {
        if (b != null) {
            buildings.add(b);
            b.setCampus(this); 
        }
    }

    public void removeBuilding(Building b) {
        if (b != null) {
            buildings.remove(b);
            b.setCampus(null); 
        }
    }

    public Set<UFR> getUfrs() {
        return Collections.unmodifiableSet(ufrs);
    }

    public void addUfr(UFR u) {
        if (u != null) {
            ufrs.add(u);
            u.setCampus(this); 
        }
    }

    public void removeUfr(UFR u) {
        if (u != null) {
            ufrs.remove(u);
            u.setCampus(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campus campus = (Campus) o;
        return Objects.equals(city, campus.city);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(city);
    }
}