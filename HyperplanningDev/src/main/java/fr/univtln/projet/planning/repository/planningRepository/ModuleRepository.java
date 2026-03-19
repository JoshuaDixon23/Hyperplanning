
package fr.univtln.projet.planning.repository.planningRepository;

import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;


public class ModuleRepository extends JpaRepository<Module, Long > {
    protected ModuleRepository(Class<Module> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }



}
