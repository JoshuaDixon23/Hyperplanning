package fr.univtln.projet.planning.entity.planning;

import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import fr.univtln.projet.planning.modele.planning.Language;

public class ModuleTest {

    @Test
    public void testCreationModule() {
        String firstName = "elisAbetH";
        String lastName = "MurisAsco";

        // Act (action)
        ProfessorEntity professor = ProfessorEntity.ProfessorFactory(firstName, lastName);

        ModuleEntity m = ModuleEntity.builder()
                .code("UE123")
                .name("developpement avancé")
                .ECTS(1)
                .responsible(professor)
                .build();

        assertEquals("UE123", m.getCode());
        assertEquals("Developpement avancé", m.getName());
        assertEquals(Language.FRENCH, m.getLanguage());
        assertEquals(1, m.getECTS());
    }
}
