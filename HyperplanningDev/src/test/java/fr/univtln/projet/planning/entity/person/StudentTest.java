package fr.univtln.projet.planning.entity.person;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    @Test
    void testCreationLocalStudent() {
        // Arrange (préparation)
        String firstName = "nikita";
        String lastName = "Rodyhin";
        String emailPersonal = "rodygin.nikita2005@gmail.com";

        // Act (action)
        LocalStudent student = LocalStudent.LocalStudentFactory(firstName, lastName, emailPersonal);

        // Assert (vérification)
        assertEquals("Nikita", student.getFirstName());
        assertEquals("RODYHIN", student.getLastName());
        assertEquals("rodygin.nikita2005@gmail.com", student.getEmailPersonal());
        assertTrue(student.getEmailUniv().matches("^nikita-rodyhin[1-9][0-9]{2}@etud.univ-tln.fr$"));
    }

    @Test
    void testCreationInternationalStudent() {
        // Arrange (préparation)
        String firstName = "nikita";
        String lastName = "Rodyhin";
        String emailPersonal = "rodygin.nikita2005@gmail.com";

        // Act (action)
        InternationalStudent student = InternationalStudent.InternationalStudentFactory(firstName, lastName, emailPersonal);

        // Assert (vérification)
        assertEquals("Nikita", student.getFirstName());
        assertEquals("RODYHIN", student.getLastName());
        assertEquals("rodygin.nikita2005@gmail.com", student.getEmailPersonal());
        assertTrue(student.getEmailUniv().matches("^nikita-rodyhin[1-9][0-9]{2}@etud.univ-tln.fr$"));
    }

}
