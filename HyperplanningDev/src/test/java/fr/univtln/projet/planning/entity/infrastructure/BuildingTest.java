package fr.univtln.projet.planning.entity.infrastructure;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuildingTest {

    @Test
    void testBuildingFactoryCreatesPromo() {


        Map<String,String> hours = new HashMap<>();
        hours.put("Monday", "9h-17h");

        Building building = Building.BuildingFactory("UFR Sciences",
                "Campus La Garde", hours);

        assertNotNull(building);
        assertEquals("UFR Sciences", building.getName());
        assertEquals("Campus La Garde", building.getLocalisation());
        assertEquals(hours, building.getOpeningHours());

    }
}
