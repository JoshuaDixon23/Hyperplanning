package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.Admin;
import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

public class AdminRepository extends JpaRepository<Admin, Long> {

    public AdminRepository(EntityManager entityManager) {
        super(Admin.class, entityManager);
    }

    public Admin findByEmailUniv(String emailUniv) {
        return em.createQuery(
                        "SELECT u FROM Admin u WHERE LOWER(u.emailUniv) = LOWER(:emailUniv) ORDER BY u.userId",
                        Admin.class)
                .setParameter("emailUniv", emailUniv)
                .getSingleResult();
    }
}
