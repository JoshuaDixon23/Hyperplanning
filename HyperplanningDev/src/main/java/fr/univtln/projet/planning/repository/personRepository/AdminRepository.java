package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.modele.person.Admin;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

public class AdminRepository extends JpaRepository<Admin, Long> {

    public AdminRepository(EntityManager entityManager) {
        super(Admin.class, entityManager);
    }
}
