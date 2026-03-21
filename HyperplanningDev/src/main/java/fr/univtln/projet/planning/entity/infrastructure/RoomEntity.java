package fr.univtln.projet.planning.entity.infrastructure;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.infrastructure.RoomType;
import lombok.Getter;

@Getter
public class RoomEntity {

    private int number;
    private int capacity;
    private RoomType type;

    private final BuildingEntity building;

    // private final Set<CourseEntity> courses = new TreeSet<>();

    //factory

    private RoomEntity(int num, int capacity, RoomType type, BuildingEntity building) {
        this.number = num;
        this.capacity = capacity;
        this.type = type;
        this.building = building;
    }

    public static RoomEntity RoomFactory(int num, int capacity, RoomType type, BuildingEntity building) {
        return new RoomEntity(num, capacity, type, building);
    }

    // getter setter

    public void setNumber(int num) {
        this.number = num;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    /*
    public Set<CourseEntity> getCourses() {
        return courses;
    }

     */

    public String getName(){
        return building.getName() + this.getNumber();
    }

    @Override
    public String toString() {
        return "RoomEntity{" +
                "number=" + number +
                ", capacity=" + capacity +
                ", type=" + type +
                ", building=" + building +
                '}';
    }

    // manage room

    /*
    public void addCourse(CourseEntity c) {
        if (c == null) {
            return; //throw ?
        }
        else {
            courses.add(c);
            c.setRoom(this);
        }
    }


    public void removeCourse(CourseEntity c) {
        if (c == null) {
            return; //throw ?
        }
        else {
            courses.remove(c);
            c.setRoom(null);
        }
    }

     */


    // equals et hashCode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoomEntity room = (RoomEntity) o;
        return Objects.equals(number, room.number) && Objects.equals(building, room.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, building);
    }



}

