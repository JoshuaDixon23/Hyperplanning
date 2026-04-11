package fr.univtln.projet.planning.entity.international;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.academic.GroupType;

public class BasketFinalTest {
    @Test
    void testBasketFinalAddAndRetrieve() {

        InternationalStudentEntity student = new InternationalStudentEntity("John", "Doe");
        student.setEmailUniv("john@univ.fr");

        BasketFinalEntity basket = new BasketFinalEntity();

        ModuleEntity module = ModuleEntity.builder()
                .name("Maths")
                .build();

        GroupEntity group = GroupEntity.GroupFactory(1, GroupType.TD);

        basket.addModuleGroup(module, group);

        assertNotNull(basket);
        assertEquals(1, basket.getModuleGroup().size());
        assertEquals(group, basket.getGroup(module));
    }}