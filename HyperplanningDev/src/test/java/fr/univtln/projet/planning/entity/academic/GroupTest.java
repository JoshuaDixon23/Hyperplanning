package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.person.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupTest {


    @Test
    void testGroupFactoryCreatesGroup() {
        GroupType type = GroupType.TD;

        Group group = Group.GroupFactory(1, type);

        assertNotNull(group);
        assertEquals(1, group.getNum());
        assertEquals(type, group.getType());
    }
}
