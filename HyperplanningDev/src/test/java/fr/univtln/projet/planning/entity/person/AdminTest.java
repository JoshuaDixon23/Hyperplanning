package fr.univtln.projet.planning.entity.person;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    @Test
    void testCreationAdmin() {
        // Arrange (préparation)
        String firstName = "Thierry";
        String lastName = "VIA";

        // Act (action)
        Admin admin = Admin.AdminFactory(firstName, lastName);

        // Assert (vérification)
        assertEquals("Thierry", admin.getFirstName());
        assertEquals("VIA", admin.getLastName());
        assertEquals("thierry.via@univ-tln.fr", admin.getEmailUniv());
    }

}
