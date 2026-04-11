package fr.univtln.projet.planning.repository.internationalRepository;

import fr.univtln.projet.planning.modele.international.BasketFinal;
import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.repository.JpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class BasketFinalRepository extends JpaRepository<BasketFinal, Long> {

    public BasketFinalRepository(EntityManager entityManager) {
        super(BasketFinal.class, entityManager);
    }

    //  Charger complètement le basket (moduleGroup + planning)
    public Optional<BasketFinal> findWithAll(Long basketId) {
        String jpql = """
        SELECT DISTINCT b FROM BasketFinal b
        LEFT JOIN FETCH b.entries e
        LEFT JOIN FETCH e.module
        LEFT JOIN FETCH e.group
        WHERE b.id = :id
    """;

        TypedQuery<BasketFinal> query = em.createQuery(jpql, BasketFinal.class);
        query.setParameter("id", basketId);

        return query.getResultStream().findFirst();
    }


    // Trouver les baskets contenant un module
    public List<BasketFinal> findByModule(ModuleEntity module) {
        String jpql = """
        SELECT DISTINCT b FROM BasketFinal b
        JOIN b.entries e
        WHERE e.module = :module
    """;

        TypedQuery<BasketFinal> query = em.createQuery(jpql, BasketFinal.class);
        query.setParameter("module", module);

        return query.getResultList();
    }

    // Vérifier si un module est dans un basket
    public boolean containsModule(Long basketId, ModuleEntity module) {
        String jpql = """
        SELECT COUNT(e) FROM BasketEntry e
        WHERE e.basket.id = :id AND e.module = :module
    """;

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("id", basketId);
        query.setParameter("module", module);

        return query.getSingleResult() > 0;
    }
}