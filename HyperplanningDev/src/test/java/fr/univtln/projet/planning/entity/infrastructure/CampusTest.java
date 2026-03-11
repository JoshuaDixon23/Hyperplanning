package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.academic.UFR;
import fr.univtln.projet.planning.entity.infrastructure.Campus;
import fr.univtln.projet.planning.entity.person.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CampusTest {
    @Test
    void testCampusFactoryCreatesPromo() {
        //création UFR
        String city ="city";
        String imageFileName = "imageFileName";
        Campus campus = Campus.CampusFactory(city,imageFileName);



        assertNotNull(campus);
        assertEquals(city, campus.getCity());
        assertEquals(imageFileName, campus.getImageFileName());
    }
}
