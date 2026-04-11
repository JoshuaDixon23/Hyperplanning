package fr.univtln.projet.planning.entity.international;

import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.entity.international.BasketModuleEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasketModuleTest {

    @Test
    void testBasketModuleAddAndRetrieveModules() {
        //Create student international
        InternationalStudentEntity student = new InternationalStudentEntity("John", "Doe");

        //Create Basket empty
        BasketModuleEntity basket = new BasketModuleEntity(student);

        //New Module
        Module module = new Module();
        module.setName("Maths");

        //Starting basket
        basket.addModule(module);

        assertNotNull(basket);
        assertEquals(student, basket.getInternationalStudentEntity());
        assertEquals(1, basket.getModules().size());
        assertTrue(basket.getModules().contains(module));
    }
}