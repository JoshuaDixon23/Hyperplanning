package fr.univtln.projet.planning.service.authenticationService;  // Correction du package pour correspondre au dossier renommé

import fr.univtln.projet.planning.modele.authentication.Authentication;
import fr.univtln.projet.planning.service.authenticationService.AuthenticationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthentificationServiceTest {

//    private EntityManagerFactory emf;
//    private EntityManager em;
//    private AuthenticationService authService;
//
//    @BeforeEach
//    public void setUp() {
//        try {
//            System.out.println("setUp called");
//            emf = Persistence.createEntityManagerFactory("HyperplanningPU");
//            em = emf.createEntityManager();
//            authService = new AuthenticationService(em);
//
//            // Nettoyage
//            em.getTransaction().begin();
//            em.createQuery("DELETE FROM Authentication a WHERE a.email = :email")
//                    .setParameter("email", "test@example.com")
//                    .executeUpdate();
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    @Test
//    public void testRegisterValidEmail() {
//        // Test d'enregistrement avec un email valide
//        String testEmail = "test@example.com";
//        Optional<Authentication> result = authService.register(testEmail);
//
//        // Vérifications
//        assertTrue(result.isPresent(), "L'enregistrement devrait réussir");
//        assertEquals(testEmail, result.get().getEmail(), "L'email devrait correspondre");
//        assertFalse(result.get().isPasswordDefined(), "Le mot de passe ne devrait pas être défini");
//    }
//
//    @AfterEach
//    public void tearDown() {
//        em.close();
//        emf.close();
//    }
}
