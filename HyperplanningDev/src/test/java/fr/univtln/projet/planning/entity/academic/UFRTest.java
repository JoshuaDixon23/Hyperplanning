package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.infrastructure.Campus;
import fr.univtln.projet.planning.entity.person.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UFRTest {
    @Test
    void testUFRFactoryCreatesPromo() {
        //création UFR
        String city ="city";
        String imageFileName = "imageFileName";
        Campus campus = Campus.CampusFactory(city,imageFileName);

        String firstName = "Thierry";
        String lastName = "VIA";
        Admin admin = Admin.AdminFactory(firstName, lastName);

        String ScienceEtTech = "ScienceEtTech";
        UFR ufr = UFR.UFRFactory(ScienceEtTech,campus,admin);

        assertNotNull(ufr);
        assertEquals(ScienceEtTech, ufr.getName());
        assertEquals(campus, ufr.getCampus());
        assertEquals(admin, ufr.getAdmin());
    }

}
