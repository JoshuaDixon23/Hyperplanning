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
            LEFT JOIN FETCH b.moduleGroup mg
            LEFT JOIN FETCH b.planning p
            WHERE b.id = :id
        """;

        TypedQuery<BasketFinal> query = em.createQuery(jpql, BasketFinal.class);
        query.setParameter("id", basketId);

        List<BasketFinal> result = query.getResultList();
        return result.stream().findFirst();
    }


    // Trouver les baskets contenant un module
    public List<BasketFinal> findByModule(ModuleEntity module) {
        String jpql = """
            SELECT DISTINCT b FROM BasketFinal b
            JOIN b.moduleGroup mg
            WHERE KEY(mg) = :module
        """;

        TypedQuery<BasketFinal> query = em.createQuery(jpql, BasketFinal.class);
        query.setParameter("module", module);

        return query.getResultList();
    }

    // 🔥 Vérifier si un module est dans un basket
    public boolean containsModule(Long basketId, ModuleEntity module) {
        String jpql = """
            SELECT COUNT(b) FROM BasketFinal b
            JOIN b.moduleGroup mg
            WHERE b.id = :id AND KEY(mg) = :module
        """;

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("id", basketId);
        query.setParameter("module", module);

        return query.getSingleResult() > 0;
    }
}