package fr.univtln.projet.planning.service.authenticationService;  // Correction du package pour correspondre au dossier renommé

import fr.univtln.projet.planning.modele.authentication.Authentication;
import fr.univtln.projet.planning.repository.authenticationRepository.AuthenticationRepository;
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





    @Test
    public void testRegisterValidEmail() {


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        AuthenticationRepository authRepo = new AuthenticationRepository(em);
        AuthenticationService authService = new AuthenticationService(em,authRepo);

        // Test d'enregistrement avec un email valide
        String testEmail = "thomas@dj.com";
        Optional<Authentication> result = authService.register(testEmail);

        // Vérifications
        assertTrue(result.isPresent(), "L'enregistrement devrait réussir");
        assertEquals(testEmail, result.get().getEmail(), "L'email devrait correspondre");
        assertFalse(result.get().isPasswordDefined(), "Le mot de passe ne devrait pas être défini");
    }


//    @Test
//    public void testSetPassword() {
//
//
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
//        EntityManager em = emf.createEntityManager();
//
//        AuthenticationRepository authRepo = new AuthenticationRepository(em);
//        AuthenticationService authService = new AuthenticationService(em,authRepo);
//
//        // Test d'enregistrement avec un email valide
//        String testEmail = "thomas@dj.com";
//        Optional<Authentication> result = authService.register(testEmail);
//
//        // Vérifications
//        assertTrue(result.isPresent(), "L'enregistrement devrait réussir");
//        assertEquals(testEmail, result.get().getEmail(), "L'email devrait correspondre");
//        assertFalse(result.get().isPasswordDefined(), "Le mot de passe ne devrait pas être défini");
//    }

//    @Test
//    public void testverifyPassword() {
//
//
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
//        EntityManager em = emf.createEntityManager();
//
//        AuthenticationRepository authRepo = new AuthenticationRepository(em);
//        AuthenticationService authService = new AuthenticationService(em,authRepo);
//
//        // Test d'enregistrement avec un email valide
//        String testEmail = "thomas@dj.com";
//        Optional<Authentication> result = authService.register(testEmail);
//
//        // Vérifications
//        assertTrue(result.isPresent(), "L'enregistrement devrait réussir");
//        assertEquals(testEmail, result.get().getEmail(), "L'email devrait correspondre");
//        assertFalse(result.get().isPasswordDefined(), "Le mot de passe ne devrait pas être défini");
//    }





}
