package fr.univtln.projet.planning.repository.academicRepository;

import java.util.List;
import java.util.Optional;

import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PromoRepository extends JpaRepository<Promo,Long> {


    protected PromoRepository(Class<Promo> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    // Sauvegarder ou mettre à jour une Promo
    public Promo save(Promo promo) {
        super.em.getTransaction().begin();
        if (promo.getPromoId() == null) {
            super.em.persist(promo); // Nouvelle promo
        } else {
            promo = super.em.merge(promo); // Mise à jour
        }
        super.em.getTransaction().commit();
        return promo;
    }

    // Trouver par ID
    public Optional<Promo> findById(Long id) {
        return Optional.ofNullable(super.em.find(Promo.class, id));
    }

    // Récupérer toutes les promos
    public List<Promo> findAll() {
        String jpql = "SELECT p FROM Promo p";
        TypedQuery<Promo> query = super.em.createQuery(jpql, Promo.class);
        return query.getResultList();
    }

    // Exemple de JPQL spécifique : Trouver les promos par nom
    public List<Promo> findByName(String name) {
        String jpql = "SELECT p FROM Promo p WHERE p.name = :name";
        TypedQuery<Promo> query = super.em.createQuery(jpql, Promo.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}