
package fr.univtln.projet.planning.repository.planningRepository;

import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;


public class ModuleRepository extends JpaRepository<Module, Long > {


    protected ModuleRepository(EntityManager entityManager) {
        super(Module.class, entityManager);
    }
}
