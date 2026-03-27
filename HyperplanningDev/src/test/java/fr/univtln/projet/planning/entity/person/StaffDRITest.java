package fr.univtln.projet.planning.entity.person;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StaffDRITest {

    @Test
    void testCreationProfessor() {
        // Arrange (préparation)
        String firstName = "bRigitte";
        String lastName = "Boucand";

        // Act (action)
        StaffDRIEntity staffDRI = StaffDRIEntity.StaffDRIFactory(firstName, lastName);

        // Assert (vérification)
        assertEquals("Brigitte", staffDRI.getFirstName());
        assertEquals("BOUCAND", staffDRI.getLastName());
        assertTrue(staffDRI.getEmailUniv().matches("^brigitte.boucand[1-9]@univ-tln.fr$"));
    }

}
