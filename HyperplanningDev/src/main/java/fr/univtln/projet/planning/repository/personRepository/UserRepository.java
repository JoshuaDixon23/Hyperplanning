package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class UserRepository extends JpaRepository<User,Long> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public List<User> findByLastName(int pageNumber, int pageSize, String lastName) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Pagination invalide");
        }
        return em.createQuery(
                        "SELECT u FROM User u WHERE UPPER(u.lastName) = UPPER(:lastName) ORDER BY u.userId",
                        User.class)
                .setParameter("lastName", lastName)
                .setFirstResult(pageNumber*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Recherche un utilisateur par email universitaire
     */
    public Optional<User> findByEmailUniv(String emailUniv) {
        try {
            return Optional.ofNullable(
                    em.createQuery(
                            "SELECT u FROM User u WHERE u.emailUniv = :emailUniv",
                            User.class)
                            .setParameter("emailUniv", emailUniv)
                            .getSingleResult()
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Vérifie si un utilisateur avec cet email existe
     */
    public boolean existsByEmailUniv(String emailUniv) {
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.emailUniv = :emailUniv",
                    Long.class)
                    .setParameter("emailUniv", emailUniv)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retourne le mot de passe hashedPassword pour un utilisateur (seulement pour vérification)
     * Cette méthode ne doit être utilisée que par le service d'authentification
     */
    public Optional<String> getHashedPasswordByEmailUniv(String emailUniv) {
        try {
            String hashedPassword = em.createQuery(
                    "SELECT u.hashedPassword FROM User u WHERE u.emailUniv = :emailUniv",
                    String.class)
                    .setParameter("emailUniv", emailUniv)
                    .getSingleResult();
            return Optional.ofNullable(hashedPassword);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Vérifie si un mot de passe est défini pour un utilisateur
     */
    public boolean isPasswordDefinedByEmailUniv(String emailUniv) {
        try {
            Boolean isDefined = em.createQuery(
                    "SELECT u.passwordDefined FROM User u WHERE u.emailUniv = :emailUniv",
                    Boolean.class)
                    .setParameter("emailUniv", emailUniv)
                    .getSingleResult();
            return isDefined != null && isDefined;
        } catch (Exception e) {
            return false;
        }
    }
}
