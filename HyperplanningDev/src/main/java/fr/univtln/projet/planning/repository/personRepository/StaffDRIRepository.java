package fr.univtln.projet.planning.repository.personRepository;

import fr.univtln.projet.planning.repository.JpaRepository;
import fr.univtln.projet.planning.modele.person.StaffDRI;
import jakarta.persistence.EntityManager;

public class StaffDRIRepository extends JpaRepository<StaffDRI, Long> {

    public StaffDRIRepository(EntityManager entityManager) {
        super(StaffDRI.class, entityManager);
    }
}
