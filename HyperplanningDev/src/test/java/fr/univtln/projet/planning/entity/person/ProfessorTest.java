package fr.univtln.projet.planning.entity.person;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProfessorTest {

    @Test
    void testCreationProfessor() {
        // Arrange (préparation)
        String firstName = "elisAbetH";
        String lastName = "MurisAsco";

        // Act (action)
        Professor professor = Professor.ProfessorFactory(firstName, lastName);

        // Assert (vérification)
        assertEquals("Elisabeth", professor.getFirstName());
        assertEquals("MURISASCO", professor.getLastName());
        assertEquals("elisabeth.murisasco@univ-tln.fr", professor.getEmailUniv());
    }

}
