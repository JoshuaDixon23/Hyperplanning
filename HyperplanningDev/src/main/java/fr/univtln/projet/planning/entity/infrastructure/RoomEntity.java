package fr.univtln.projet.planning.entity.infrastructure;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.infrastructure.RoomType;

/**
 * Ajout de cours bizarre j'ai prefe ne pas faire
 *
 */
public class RoomEntity {

    private int num;
    private int capacity;
    private RoomType type;

    private final BuildingEntity building;

    private final Set<CourseEntity> courses = new TreeSet<>();

    //factory

    private RoomEntity(int num, int capacity, RoomType type, BuildingEntity building) {
        this.num = num;
        this.capacity = capacity;
        this.type = type;
        this.building = building;
    }

    public static RoomEntity RoomFactory(int num, int capacity, RoomType type, BuildingEntity building) {
        return new RoomEntity(num, capacity, type, building);
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

    public BuildingEntity getBuilding() {
        return building;
    }

    public Set<CourseEntity> getCourses() {
        return courses;
    }

    public String getName(){
        return building.getName() + this.getNum();
    }

    // manage room

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




    // equals et hashCode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoomEntity room = (RoomEntity) o;
        return Objects.equals(num, room.num) && Objects.equals(building, room.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, building);
    }



}

