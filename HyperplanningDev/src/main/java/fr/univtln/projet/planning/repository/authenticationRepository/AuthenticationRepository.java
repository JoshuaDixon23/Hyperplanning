package fr.univtln.projet.planning.repository.authenticationRepository;

import fr.univtln.projet.planning.modele.authentication.Authentication;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class AuthenticationRepository extends JpaRepository<Authentication, Long> {

    public AuthenticationRepository(EntityManager entityManager) {
        super(Authentication.class, entityManager);
    }

    /**
     * Recherche une authentification par email
     */


    public Optional<Authentication> findByEmail(String email) {

        String jpql = "SELECT a FROM Authentication a WHERE LOWER(a.email) = LOWER(:email)";
        TypedQuery<Authentication> query = em.createQuery(jpql, Authentication.class);
        query.setParameter("email", email);
        Authentication result = query.getSingleResult();
        return Optional.of(result);

        // Au cas ou email introuvable ajouter une condition !

    }

    /**
     * Récupère toutes les authentifications par ordre de création
     */
    public List<Authentication> findAll() {
        String jpql = "SELECT a FROM Authentication a ORDER BY a.createdAt DESC";
        TypedQuery<Authentication> query = em.createQuery(jpql, Authentication.class);
        return query.getResultList();
    }

    /**
     * Récupère toutes les authentifications sans mot de passe défini
     */
    public List<Authentication> findByPasswordNotDefined() {
        String jpql = "SELECT a FROM Authentication a WHERE a.passwordDefined = false ORDER BY a.createdAt ASC";
        TypedQuery<Authentication> query = em.createQuery(jpql, Authentication.class);
        return query.getResultList();
    }

    /**
     * Vérifie si une authentification existe pour un email
     */
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}

