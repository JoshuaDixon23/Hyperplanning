package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.planning.Course;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Ajout de cours bizarre j'ai prefe ne pas faire
 *
 */
public class Room {

    private int num;
    private int capacity;
    private RoomType type;

    private final Building building;

    private final Set<Course> courses = new TreeSet<>();

    //factory

    private Room(int num, int capacity, RoomType type, Building building) {
        this.num = num;
        this.capacity = capacity;
        this.type = type;
        this.building = building;
    }

    public static Room RoomFactory(int num, int capacity, RoomType type, Building building) {
        return new Room(num, capacity, type, building);
    }

    // getter setter

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public Building getBuilding() {
        return building;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public String getName(){
        return building.getName() + this.getNum();
    }

    // manage room

    public void addCourse(Course c) {
        if (c == null) {
            return; //throw ?
        }
        else {
            courses.add(c);
            c.setRoom(this);
        }
    }


    public void removeCourse(Course c) {
        if (c == null) {
            return; //throw ?
        }
        else {
            courses.remove(c);
            c.setRoom(null);
        }
    }




    // equals et hashCode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(num, room.num) && Objects.equals(building, room.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, building);
    }



}

