package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;
import fr.univtln.projet.planning.entity.person.AdminEntity;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PromoTest {

    @Test
    void testPromoFactoryCreatesPromo() {
        //création UFR
        CampusEntity campus = CampusEntity.CampusFactory("city","imageFileName");

        String firstName = "Thierry";
        String lastName = "VIA";
        AdminEntity admin = AdminEntity.AdminFactory(firstName, lastName);

        UFREntity ufr = UFREntity.UFRFactory("ScienceEtTech",campus,admin);

        //création promo
        StudyLevel lvl =  StudyLevel.M1;
        String name = "Informatique";
        PromoEntity promo = PromoEntity.PromoFactory(name, lvl,ufr);

        assertNotNull(promo);
        assertEquals(name, promo.getName());
        assertEquals(lvl, promo.getStudyLevel());
    }
}
