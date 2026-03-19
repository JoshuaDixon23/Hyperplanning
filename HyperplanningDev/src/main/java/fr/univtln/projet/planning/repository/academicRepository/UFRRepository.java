package fr.univtln.projet.planning.repository.academicRepository;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

public class UFRRepository extends JpaRepository<UFR, Long > {

    protected UFRRepository(Class<UFR> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }



}
