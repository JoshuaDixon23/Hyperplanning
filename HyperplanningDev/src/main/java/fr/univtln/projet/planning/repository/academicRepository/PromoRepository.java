package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List;

import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PromoRepository extends JpaRepository<Promo, Long> {

    public PromoRepository(EntityManager entityManager) {
        super(Promo.class, entityManager);
    }

    public List<Promo> findAll() {
        String jpql = "SELECT p FROM Promo p";
        TypedQuery<Promo> query = em.createQuery(jpql, Promo.class);
        return query.getResultList();
    }

    public List<Promo> findByName(String name) {
        String jpql = "SELECT p FROM Promo p WHERE p.name = :name";
        TypedQuery<Promo> query = em.createQuery(jpql, Promo.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}