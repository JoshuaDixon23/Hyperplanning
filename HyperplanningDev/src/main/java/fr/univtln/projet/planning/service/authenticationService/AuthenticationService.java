package fr.univtln.projet.planning.service.authenticationService;

import at.favre.lib.crypto.bcrypt.BCrypt;
import fr.univtln.projet.planning.entity.authentication.AuthenticationEntity;
import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.repository.personRepository.UserRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;

/**
 * Service d'authentification gérant la connexion et la gestion des mots de passe des utilisateurs
 * - Travaille directement avec la table User (qui contient hashedPassword et passwordDefined)
 * - Le mot de passe est stocké en haché en base de données
 * - La première authentification d'un utilisateur sans mdp sert à le définir
 * - Ne expose JAMAIS le hash du mot de passe
 */
public class AuthenticationService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public AuthenticationService(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
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
     * Définit le mot de passe pour la première fois
     */
    public Optional<AuthenticationEntity> setPasswordFirstTime(String email, String plainPassword) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }

        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }

        Optional<User> userOpt = userRepository.findByEmailUniv(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'email: " + email);
        }

        User user = userOpt.get();
        if (user.isPasswordDefined()) {
            throw new IllegalArgumentException("Le mot de passe a déjà été défini pour cet utilisateur");
        }

        String hashedPassword = hashPassword(plainPassword);
        user.setHashedPassword(hashedPassword);

        entityManager.getTransaction().begin();
        try {
            entityManager.merge(user);
            entityManager.getTransaction().commit();
            return Optional.of(new AuthenticationEntity(email));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Erreur lors de la définition du mot de passe: " + e.getMessage(), e);
        }
    }

    /**
     * Authentifie un utilisateur avec son email et mot de passe
     */
    public Optional<AuthenticationEntity> authenticate(String email, String plainPassword) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }

        Optional<User> userOpt = userRepository.findByEmailUniv(email);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        if (!user.isPasswordDefined()) {
            throw new IllegalArgumentException("Aucun mot de passe défini pour cet utilisateur. Définissez d'abord votre mot de passe.");
        }

        Optional<String> hashedPasswordOpt = userRepository.getHashedPasswordByEmailUniv(email);
        if (hashedPasswordOpt.isEmpty()) {
            return Optional.empty();
        }

        if (!verifyPassword(plainPassword, hashedPasswordOpt.get())) {
            return Optional.empty();
        }

        System.out.println("mdp vérifié pour : " + email);
        return Optional.of(new AuthenticationEntity(email));
    }

    /**
     * Change le mot de passe d'un utilisateur existant
     */
    public Optional<AuthenticationEntity> changePassword(String email, String oldPassword, String newPassword) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nouveau mot de passe ne peut pas être vide");
        }

        Optional<User> userOpt = userRepository.findByEmailUniv(email);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        if (!user.isPasswordDefined()) {
            throw new IllegalArgumentException("Aucun mot de passe défini pour cet utilisateur");
        }

        Optional<String> hashedPasswordOpt = userRepository.getHashedPasswordByEmailUniv(email);
        if (hashedPasswordOpt.isEmpty() || !verifyPassword(oldPassword, hashedPasswordOpt.get())) {
            return Optional.empty();
        }

        String newHashedPassword = hashPassword(newPassword);
        user.setHashedPassword(newHashedPassword);

        entityManager.getTransaction().begin();
        try {
            entityManager.merge(user);
            entityManager.getTransaction().commit();
            return Optional.of(new AuthenticationEntity(email));
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Erreur lors du changement de mot de passe: " + e.getMessage(), e);
        }
    }

    /**
     * Vérifie si un email est déjà présent dans la base
     */
    public boolean isEmailExiste(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }
        return userRepository.existsByEmailUniv(email);
    }

    /**
     * Vérifie si un utilisateur a défini son mot de passe
     */
    public boolean isPasswordDefined(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }
        return userRepository.isPasswordDefinedByEmailUniv(email);
    }
}

