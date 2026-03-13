package fr.univtln.projet.planning.modele.infrastructure;

import fr.univtln.projet.planning.modele.planning.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Room")
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRoom;

    @Column(nullable = false, length = 50)
    private String number;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type; 

    @ManyToOne
    @JoinColumn(name = "idBuilding", nullable = false)
    private Building building;

    @OneToMany(mappedBy = "room")
    private Set<Course> courses = new HashSet<>(); 


    protected Room() {
    }

    private Room(String number, int capacity, RoomType type, Building building) {
        this.number = number;
        this.capacity = capacity;
        this.type = type;
        this.building = building;
    }

    public static Room RoomFactory(String number, int capacity, RoomType type, Building building) {
        Objects.requireNonNull(number, "Room number cannot be null");
        Objects.requireNonNull(type, "Room type cannot be null");
        Objects.requireNonNull(building, "Room must belong to a building");
        
        return new Room(number, capacity, type, building);
    }

    public void addCourse(Course c) {
        if (c != null) {
            courses.add(c);
            c.setRoom(this);
        }
    }

    public void removeCourse(Course c) {
        if (c != null) {
            courses.remove(c);
            c.setRoom(null); 
        }
    }


    public Set<Course> getCourses() {
        return Collections.unmodifiableSet(courses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        // Two rooms are identical if they have the same number IN the same building
        return Objects.equals(number, room.number) && Objects.equals(building, room.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, building);
    }
}