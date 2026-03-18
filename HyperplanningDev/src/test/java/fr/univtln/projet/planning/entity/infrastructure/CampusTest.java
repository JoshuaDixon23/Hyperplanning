package fr.univtln.projet.planning.entity.infrastructure;

import fr.univtln.projet.planning.entity.academic.UFREntity;
import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;
import fr.univtln.projet.planning.entity.person.AdminEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CampusTest {
    @Test
    void testCampusFactoryCreatesPromo() {
        //création UFR
        String city ="city";
        String imageFileName = "imageFileName";
        CampusEntity campus = CampusEntity.CampusFactory(city,imageFileName);



        assertNotNull(campus);
        assertEquals(city, campus.getCity());
        assertEquals(imageFileName, campus.getImageFileName());
    }
}
