package fr.univtln.projet.planning.entity.infrastructure;

import java.time.LocalTime; // si ok ?
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Building {

    private String localisation;
    private LocalTime openingTime;
    private LocalTime closingTime;

    private Map map;
    private final Set<Room> rooms = new HashSet<>();


    //fatcory
    private Building(String localisation, LocalTime openingTime, LocalTime closingTime) {
        this.localisation = localisation;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public static Building BuildingFactory(String localisation, LocalTime openingTime, LocalTime closingTime) {
        return new Building(localisation, openingTime, closingTime);
    }
}
