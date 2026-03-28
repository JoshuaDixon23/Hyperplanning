package fr.univtln.projet.planning.service.authenticationService;

import at.favre.lib.crypto.bcrypt.BCrypt;
import fr.univtln.projet.planning.modele.authentication.Authentication;
import fr.univtln.projet.planning.repository.authenticationRepository.AuthenticationRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;

/**
 * Service d'authentification gérant l'enregistrement et la connexion des utilisateurs
 * - L'identifiant doit être un email
 * - Le mot de passe est stocké en haché dans la bd
 * - La première authentification d'un utilisateur sans mdp sert à le définir
 */
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final EntityManager entityManager;

    public AuthenticationService(EntityManager entityManager,AuthenticationRepository authenticationRepository) {
        this.entityManager = entityManager;
        this.authenticationRepository =  authenticationRepository;
    }

    /**
     * Valide le format d'un email
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Hash un mot de passe avec BCrypt
     */
    private String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    /**
     * Vérifie si un mot de passe correspond au hash stocké
     */
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }

    /**
     * Enregistre un nouvel utilisateur avec un email
     * peut être enlevé si on considère que tout les email user seront dans la bd ,ou peut etre utiliser
     * justement pour le faire
     */
    public Optional<Authentication> register(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }

        if (authenticationRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Cet email est déjà utilisé: " + email);
        }

        Authentication auth = new Authentication(email);
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(auth);
            entityManager.getTransaction().commit();
            return Optional.of(auth);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Erreur lors de l'enregistrement: " + e.getMessage(), e);
        }
    }

    /**
     * Définit le mot de passe pour la première fois
     * en verifiant deux confitions mdp pas definie et email valide
     */
    public Optional<Authentication> setPasswordFirstTime(String email, String plainPassword) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }


        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }

        //email valid mais non trouvé

        Optional<Authentication> authOpt = authenticationRepository.findByEmail(email);
        if (authOpt.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'email: " + email);
        }

        Authentication auth = authOpt.get();
        if (auth.isPasswordDefined()) {
            throw new IllegalArgumentException("Le mot de passe a déjà été défini pour cet utilisateur");
        }

        String hashedPassword = hashPassword(plainPassword);
        auth.setHashedPassword(hashedPassword);

        entityManager.getTransaction().begin();


        // cool ou pas cool ?
        try {
            entityManager.merge(auth);
            entityManager.getTransaction().commit();
            return Optional.of(auth);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Erreur lors de la définition du mot de passe: " + e.getMessage(), e);
        }
    }

    /**
     * Authentifie un utilisateur avec son email et mot de passe
     */

    public Optional<Authentication> authenticate(String email, String plainPassword) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }

        Optional<Authentication> authOpt = authenticationRepository.findByEmail(email);
        if (authOpt.isEmpty()) {
            return Optional.empty();
        }

        Authentication auth = authOpt.get();

        if (!auth.isPasswordDefined()) {
            throw new IllegalArgumentException("Aucun mot de passe défini pour cet utilisateur. Définissez d'abord votre mot de passe.");
        }

        if (!verifyPassword(plainPassword, auth.getHashedPassword())) {
            return Optional.empty();
        }

        entityManager.getTransaction().begin();
        try {
            entityManager.merge(auth);
            entityManager.getTransaction().commit();
            System.out.println("mdp vérifié");
            return Optional.of(auth);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Erreur lors de la mise à jour de lastLogin: " + e.getMessage(), e);
        }
    }

    /**
     * Change le mot de passe d'un utilisateur existant
     */
    public Optional<Authentication> changePassword(String email, String oldPassword, String newPassword) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nouveau mot de passe ne peut pas être vide");
        }

        Optional<Authentication> authOpt = authenticationRepository.findByEmail(email);
        if (authOpt.isEmpty()) {
            return Optional.empty();
        }

        Authentication auth = authOpt.get();

        if (!auth.isPasswordDefined()) {
            throw new IllegalArgumentException("Aucun mot de passe défini pour cet utilisateur");
        }

        if (!verifyPassword(oldPassword, auth.getHashedPassword())) {
            return Optional.empty();
        }

        String hashedPassword = hashPassword(newPassword);
        auth.setHashedPassword(hashedPassword);

        entityManager.getTransaction().begin();
        try {
            entityManager.merge(auth);
            entityManager.getTransaction().commit();
            return Optional.of(auth);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Erreur lors du changement de mot de passe: " + e.getMessage(), e);
        }
    }

    /**
     * Récupère les détails d'authentification d'un utilisateur par son email
     */
    public Optional<Authentication> getAuthenticationByEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }
        return authenticationRepository.findByEmail(email);
    }

    /**
     * Vérifie si un email est déjà présent dans la base
     */
    public boolean isEmailExiste(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }
        return authenticationRepository.findByEmail(email).isPresent();
    }

    /**
     * Vérifie si un utilisateur a défini son mot de passe
     */
    public boolean isPasswordDefined(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }
        Optional<Authentication> authOpt = authenticationRepository.findByEmail(email);
        return authOpt.isPresent() && authOpt.get().isPasswordDefined();
    }

    /**
     * Récupère l'authentification par ID
     */
    public Optional<Authentication> getAuthenticationById(Long authenticationId) {
        return authenticationRepository.findById(authenticationId);
    }
}

