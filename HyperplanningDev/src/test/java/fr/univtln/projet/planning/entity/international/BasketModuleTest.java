package fr.univtln.projet.planning.entity.international;
import fr.univtln.projet.planning.entity.person.InternationalStudent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasketModuleTest {

    @Test
    void testAddModule() {

        // Arrange (préparation)
        InternationalStudent student = new InternationalStudent();
        BasketModule basketModule = new BasketModule(student);

        Module module = new Module("M1", "AI", "EN", 6);

        // Act (action)
        basketModule.addModule(module);

        // Assert (vérification)
        assertTrue(basketModule.getModules().contains(module));
    }
