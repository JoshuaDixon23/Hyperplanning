
package fr.univtln.projet.planning.entity.infrastructure;

import java.util.Objects;

/**
 * Ajout de cours bizarre j'ai prefe ne pas faire
 *
 */
public class Room {

    private String num;
    private int capacity;
    private RoomType type;

    private Building building;

    //private Set<Course> courses;

    //factory

    private Room(String num, int capacity, RoomType type) {
        this.num = num;
        this.capacity = capacity;
        this.type = type;
    }

    public static Room RoomFactory(String num, int capacity, RoomType type) {
        return new Room(num, capacity, type);
    }

    // getter setter

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
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

    public void setBuilding(Building building) {
        this.building = building;
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
