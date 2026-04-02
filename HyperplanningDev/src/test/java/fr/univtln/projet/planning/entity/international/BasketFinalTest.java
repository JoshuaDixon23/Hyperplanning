package fr.univtln.projet.planning.entity.international;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.international.BasketFinal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasketFinalTest {
    @Test
    void testBasketFinalAddAndRetrieve() {

        InternationalStudentEntity student = new InternationalStudentEntity("John", "Doe");
        student.setEmailUniv("john@univ.fr");

        BasketFinalEntity basket = new BasketFinalEntity();

        ModuleEntity module = new ModuleEntity("Maths");

        GroupEntity group = new GroupEntity();
        group.setNum(1);

        basket.addModuleGroup(module, group);

        assertNotNull(basket);
        assertEquals(1, basket.getModuleGroup().size());
        assertEquals(group, basket.getGroup(module));
    }}