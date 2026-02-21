package fr.univtln.projet.planning.entity.person;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    @Test
    void testCreationStudent() {
        // Arrange (préparation)
        String firstName = "nikita";
        String lastName = "Rodyhin";
        String emailPersonal = "rodygin.nikita2005@gmail.com";

        // Act (action)
        Student student = Student.StudentFactory(firstName, lastName, emailPersonal);

        // Assert (vérification)
        assertEquals("Nikita", student.getFirstName());
        assertEquals("RODYHIN", student.getLastName());
        assertEquals("rodygin.nikita2005@gmail.com", student.getEmailPersonal());
        assertEquals("nikita-rodyhin@etud.univ-tln.fr", student.getEmailUniv());
    }

}
