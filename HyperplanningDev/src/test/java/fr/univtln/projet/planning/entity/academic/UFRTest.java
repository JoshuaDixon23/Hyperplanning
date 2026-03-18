package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;
import fr.univtln.projet.planning.entity.person.AdminEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UFRTest {
    @Test
    void testUFRFactoryCreatesPromo() {
        //création UFR
        String city ="city";
        String imageFileName = "imageFileName";
        CampusEntity campus = CampusEntity.CampusFactory(city,imageFileName);

        String firstName = "Thierry";
        String lastName = "VIA";
        AdminEntity admin = AdminEntity.AdminFactory(firstName, lastName);

        String ScienceEtTech = "ScienceEtTech";
        UFREntity ufr = UFREntity.UFRFactory(ScienceEtTech,campus,admin);

        assertNotNull(ufr);
        assertEquals(ScienceEtTech, ufr.getName());
        assertEquals(campus, ufr.getCampus());
        assertEquals(admin, ufr.getAdmin());
    }

}
