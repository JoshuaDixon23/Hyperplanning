package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.modele.academic.GroupType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupTest {


    @Test
    void testGroupFactoryCreatesGroup() {
        GroupType type = GroupType.TD;

        GroupEntity group = GroupEntity.GroupFactory(1, type);

        assertNotNull(group);
        assertEquals(1, group.getNum());
        assertEquals(type, group.getType());
    }
}
