package fr.univtln.projet.planning.entity.international;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.international.BasketFinal;
import fr.univtln.projet.planning.modele.planning.Module;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasketEntryTest {

    @Test
    void testBasketEntryCreation() {

        // 1. Création des objets nécessaires
        BasketFinal basket = new BasketFinal();

        Module module = new Module();
        module.setName("Maths");

        Group group = new Group();
        group.setNum(1);

        // 2. Création de l’entrée
        BasketEntryEntity entry = new BasketEntryEntity(basket, module, group);

        // 3. Vérifications
        assertNotNull(entry);
        assertEquals(basket, entry.getBasket());
        assertEquals(module, entry.getModule());
        assertEquals(group, entry.getGroup());
    }
}