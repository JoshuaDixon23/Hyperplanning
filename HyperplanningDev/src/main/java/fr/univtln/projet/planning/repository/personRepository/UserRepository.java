package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.UserEntity;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class UserRepository extends JpaRepository<UserEntity,Long> {

    public UserRepository(EntityManager entityManager) {
        super(UserEntity.class, entityManager);
    }

    public List<UserEntity> findByLastName(int pageNumber, int pageSize, String lastName) {
        return em.createQuery(
                        "SELECT u FROM UserEntity u WHERE UPPER(u.lastName) = UPPER(:lastName) ORDER BY u.userId",
                        UserEntity.class)
                .setParameter("lastName", lastName)
                .setFirstResult((pageNumber-1)*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
