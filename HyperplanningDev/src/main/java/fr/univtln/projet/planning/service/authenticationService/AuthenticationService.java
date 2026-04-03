package fr.univtln.projet.planning.service.authenticationService;

import at.favre.lib.crypto.bcrypt.BCrypt;
import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.repository.personRepository.UserRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class AuthenticationService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public AuthenticationService(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }

    // modifié pour user simple à la place de authentication entity
    public Optional<User> setPasswordFirstTime(String email, String plainPassword) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }

        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }

        if (!isValidPassword(plainPassword)) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères et un caractère spécial");
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
            User updatedUser = entityManager.merge(user);
            entityManager.getTransaction().commit();
            return Optional.of(updatedUser);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Erreur lors de la définition du mot de passe: " + e.getMessage(), e);
        }
    }

    // modifié pour user simple à la place de authentication entity
    public Optional<User> authenticate(String email, String plainPassword) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }

        Optional<User> userOpt = userRepository.findByEmailUniv(email);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        if (!user.isPasswordDefined()) {
            throw new IllegalArgumentException("Aucun mot de passe défini pour cet utilisateur.");
        }

        Optional<String> hashedPasswordOpt = userRepository.getHashedPasswordByEmailUniv(email);
        if (hashedPasswordOpt.isEmpty()) {
            return Optional.empty();
        }

        if (!verifyPassword(plainPassword, hashedPasswordOpt.get())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    // changer le mdp
    // modifié pour user simple à la place de authentication entity

    public Optional<User> changePassword(String email, String oldPassword, String newPassword) {
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

        Optional<String> hashedPasswordOpt = userRepository.getHashedPasswordByEmailUniv(email);
        if (hashedPasswordOpt.isEmpty() || !verifyPassword(oldPassword, hashedPasswordOpt.get())) {
            return Optional.empty();
        }

        String newHashedPassword = hashPassword(newPassword);
        user.setHashedPassword(newHashedPassword);

        entityManager.getTransaction().begin();
        try {
            User updatedUser = entityManager.merge(user);
            entityManager.getTransaction().commit();
            return Optional.of(updatedUser);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Erreur lors du changement de mot de passe: " + e.getMessage(), e);
        }
    }


    //vérification email valide
    public boolean isEmailExiste(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }
        return userRepository.existsByEmailUniv(email);
    }

    public boolean isPasswordDefined(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Format d'email invalide: " + email);
        }
        return userRepository.isPasswordDefinedByEmailUniv(email);
    }


    // verification d'un mdp valide minimum 8 caractères + 1 caractère spécial


    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        return password != null && password.matches(regex);
    }
}