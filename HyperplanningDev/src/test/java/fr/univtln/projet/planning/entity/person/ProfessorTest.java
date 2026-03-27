package fr.univtln.projet.planning.entity.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ProfessorTest {

    @Test
    void testCreationProfessor() {
        // Arrange (préparation)
        String firstName = "elisAbetH";
        String lastName = "MurisAsco";

        // Act (action)
        ProfessorEntity professor = ProfessorEntity.ProfessorFactory(firstName, lastName);

        // Assert (vérification)
        assertEquals("Elisabeth", professor.getFirstName());
        assertEquals("MURISASCO", professor.getLastName());
        assertTrue(professor.getEmailUniv().matches("^elisabeth.murisasco[1-9]@univ-tln.fr$"));
    }

}
