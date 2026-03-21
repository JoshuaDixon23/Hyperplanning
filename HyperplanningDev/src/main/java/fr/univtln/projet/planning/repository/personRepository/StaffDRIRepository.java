package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.repository.JpaRepository;
import fr.univtln.projet.planning.modele.person.StaffDRI;
import jakarta.persistence.EntityManager;

public class StaffDRIRepository extends JpaRepository<StaffDRI, Long> {

    public StaffDRIRepository(EntityManager entityManager) {
        super(StaffDRI.class, entityManager);
    }

    public StaffDRI findByEmailUniv(String emailUniv) {
        return em.createQuery(
                        "SELECT u FROM StaffDRI u WHERE LOWER(u.emailUniv) = LOWER(:emailUniv) ORDER BY u.userId",
                        StaffDRI.class)
                .setParameter("emailUniv", emailUniv)
                .getSingleResult();
    }
}
