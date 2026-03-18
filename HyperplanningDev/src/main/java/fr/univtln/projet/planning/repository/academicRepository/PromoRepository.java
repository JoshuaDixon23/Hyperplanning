package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.academic.Promo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PromoRepository {

    private final EntityManager entityManager;

    public PromoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Sauvegarder ou mettre à jour une Promo
    public Promo save(Promo promo) {
        entityManager.getTransaction().begin();
        if (promo.getPromoId() == null) {
            entityManager.persist(promo); // Nouvelle promo
        } else {
            promo = entityManager.merge(promo); // Mise à jour
        }
        entityManager.getTransaction().commit();
        return promo;
    }

    // Trouver par ID
    public Optional<Promo> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Promo.class, id));
    }

    // Récupérer toutes les promos
    public List<Promo> findAll() {
        String jpql = "SELECT p FROM Promo p";
        TypedQuery<Promo> query = entityManager.createQuery(jpql, Promo.class);
        return query.getResultList();
    }

    // Exemple de JPQL spécifique : Trouver les promos par nom
    public List<Promo> findByName(String name) {
        String jpql = "SELECT p FROM Promo p WHERE p.name = :name";
        TypedQuery<Promo> query = entityManager.createQuery(jpql, Promo.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}