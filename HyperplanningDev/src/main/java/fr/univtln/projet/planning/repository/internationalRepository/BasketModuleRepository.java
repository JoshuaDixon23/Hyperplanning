package fr.univtln.projet.planning.repository.internationalRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.international.BasketModule;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class BasketModuleRepository extends JpaRepository<BasketModule, Long> {

    public BasketModuleRepository(EntityManager entityManager) {
        super(BasketModule.class, entityManager);
    }


    // find basket
    public Optional<BasketModule> findByStudent(InternationalStudentEntity student) {
        String jpql = "SELECT b FROM BasketModule b WHERE b.internationalStudent = :student";
        TypedQuery<BasketModule> query = em.createQuery(jpql, BasketModule.class);
        query.setParameter("student", student);

        List<BasketModule> result = query.getResultList();
        return result.stream().findFirst();
    }

    // find basket by module
    public Optional<BasketModule> findWithModules(Long basketId) {
        String jpql = "SELECT b FROM BasketModule b LEFT JOIN FETCH b.modules WHERE b.id = :id";
        TypedQuery<BasketModule> query = em.createQuery(jpql, BasketModule.class);
        query.setParameter("id", basketId);

        List<BasketModule> result = query.getResultList();
        return result.stream().findFirst();
    }


}