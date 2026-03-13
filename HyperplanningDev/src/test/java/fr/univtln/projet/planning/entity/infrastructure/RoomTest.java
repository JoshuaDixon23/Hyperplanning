package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.academic.UFR;
import fr.univtln.projet.planning.entity.infrastructure.Campus;
import fr.univtln.projet.planning.entity.person.Admin;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static fr.univtln.projet.planning.entity.infrastructure.Room.RoomFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoomTest {
    @Test
    void testRoomFactoryCreatesPromo() {
        //création UFR
        int num = 006;
        int capacity = 30;
        RoomType type = RoomType.AMPHITHEATER;

        Map<String,String> hours = new HashMap<>();
        hours.put("Monday", "9h-17h");

        Building building = Building.BuildingFactory("UFR Sciences",
                "Campus La Garde", hours);


        Room room = Room.RoomFactory(num, capacity, type, building);



        assertNotNull(room);
        assertEquals(num, room.getNum());
        assertEquals(capacity, room.getCapacity());
        assertEquals(type, room.getType());
        assertEquals(building, room.getBuilding());


    }
}
