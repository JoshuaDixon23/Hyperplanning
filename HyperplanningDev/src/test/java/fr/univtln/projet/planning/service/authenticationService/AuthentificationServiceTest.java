package fr.univtln.projet.planning.service.authenticationService;

import fr.univtln.projet.planning.entity.authentication.AuthenticationEntity;
import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.repository.personRepository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthentificationServiceTest {

    @Test
    public void testCreateStudentAndSetPasswordFirstTime() {
        // Créer l'EntityManagerFactory et EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        UserRepository userRepo = new UserRepository(em);
        AuthenticationService authService = new AuthenticationService(em, userRepo);

        String testEmail = "etudiant@univtln.fr";

        // 1. Créer un étudiant et le persister en base de données
        em.getTransaction().begin();
        try {
            LocalStudent student = new LocalStudent(
                    "Jean",
                    "Dupont",
                    testEmail,
                    "jean.dupont@email.com",
                    null
            );
            em.persist(student);
            em.getTransaction().commit();
            System.out.println("✓ Étudiant créé en base de données: " + testEmail);
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erreur création étudiant: " + e.getMessage(), e);
        }

        // 2. Vérifier que l'étudiant existe
        assertTrue(userRepo.existsByEmailUniv(testEmail), "L'étudiant doit exister en base");
        System.out.println("✓ Vérification: l'étudiant existe");

        // 3. Vérifier que le mot de passe n'est pas défini au départ
        assertFalse(userRepo.isPasswordDefinedByEmailUniv(testEmail), "Le mot de passe ne doit pas être défini");
        System.out.println("✓ Vérification: aucun mot de passe défini");

        // 4. Définir le mot de passe pour la première fois
        String password = "MonMotDePasse123!";
        Optional<AuthenticationEntity> result = authService.setPasswordFirstTime(testEmail, password);

        assertTrue(result.isPresent(), "La création du mot de passe devrait réussir");
        assertEquals(testEmail, result.get().getEmail(), "L'email doit correspondre");
        System.out.println("✓ Mot de passe défini avec succès");

        // 5. Vérifier que le mot de passe est maintenant défini
        assertTrue(userRepo.isPasswordDefinedByEmailUniv(testEmail), "Le mot de passe doit maintenant être défini");
        System.out.println("✓ Vérification: mot de passe défini");

        // 6. Vérifier l'authentification avec le mot de passe
        Optional<AuthenticationEntity> authenticated = authService.authenticate(testEmail, password);
        assertTrue(authenticated.isPresent(), "L'authentification doit réussir");
        System.out.println("✓ Authentification réussie avec le mot de passe");

        em.close();
        emf.close();
    }

}
