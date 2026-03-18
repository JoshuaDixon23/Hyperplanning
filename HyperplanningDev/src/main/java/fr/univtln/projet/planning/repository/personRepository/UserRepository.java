package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

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
}
