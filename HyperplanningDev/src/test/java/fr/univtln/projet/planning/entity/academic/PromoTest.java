package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.infrastructure.Campus;
import fr.univtln.projet.planning.entity.person.Admin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PromoTest {

    @Test
    void testPromoFactoryCreatesPromo() {
        //création UFR
        Campus campus = Campus.CampusFactory("city","imageFileName");

        String firstName = "Thierry";
        String lastName = "VIA";
        Admin admin = Admin.AdminFactory(firstName, lastName);

        UFR ufr = UFR.UFRFactory("ScienceEtTech",campus,admin);

        //création promo
        StudyLevel lvl =  StudyLevel.M1;
        String name = "Informatique";
        Promo promo = Promo.PromoFactory(name, lvl,ufr);

        assertNotNull(promo);
        assertEquals(name, promo.getName());
        assertEquals(lvl, promo.getStudyLevel());
    }
}
