package fr.univtln.projet.planning.modele.infrastructure;

import fr.univtln.projet.planning.modele.academic.UFR;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "Building")
@Getter
@Setter
public class Building {

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hours {
        @Column(name = "opening_time")
        private LocalTime opening;
        
        @Column(name = "closing_time")
        private LocalTime closing;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBuilding;

    @Column(nullable = false, unique = true)
    private String name;

    private String localisation;

    @ManyToOne
    @JoinColumn(name = "idCampus") 
    private Campus campus;

    @ManyToOne
    @JoinColumn(name = "ufrId")
    private UFR ufr;

    @ElementCollection
    @CollectionTable(
        name = "Building_OpeningHours",
        joinColumns = @JoinColumn(name = "idBuilding")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "dayOfWeek") 
    private Map<Day, Hours> openingHours = new EnumMap<>(Day.class); 

    //@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    //private Set<Room> rooms = new HashSet<>();

    protected Building() {
    }

    private Building(String name, String localisation, Map<Day, Hours> openingHours) { 
        this.name = name;
        this.localisation = localisation;
        if (openingHours != null) {
            this.openingHours = openingHours;
        }
    }

    public static Building BuildingFactory(String name, String localisation, Map<Day, Hours> openingHours) { 
        Objects.requireNonNull(name, "Building name cannot be null");
        return new Building(name, localisation, openingHours);
    }

    /*
    public void addRoom(Room room) {
        if (room != null) {
            rooms.add(room);
            room.setBuilding(this);
        }
    }

    public void removeRoom(Room room) {
        if (room != null) {
            rooms.remove(room);
            room.setBuilding(null);
        }
    }

    public Set<Room> getRooms() {
        return Collections.unmodifiableSet(rooms);
    }

     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return Objects.equals(name, building.name) && 
               Objects.equals(localisation, building.localisation) && 
               Objects.equals(ufr, building.ufr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, localisation, ufr);
    }
}