package fr.univtln.projet.planning.entity.international;
import fr.univtln.projet.planning.entity.academic.Group;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BasketFinalTest {

    @Test
    void testAddModuleGroup() {

        // Arrange (préparation)
        BasketFinal basketFinal = new BasketFinal();

        String module = "M1";
        String group = "TD1";

        // Act (action)
        basketFinal.addModuleGroup(module, group);

        // Assert (vérification)
        assertEquals(group, basketFinal.getGroup(module));
        assertTrue(basketFinal.getModuleGroup().containsKey(module));
    }