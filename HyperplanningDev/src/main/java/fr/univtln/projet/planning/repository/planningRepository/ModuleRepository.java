
package fr.univtln.projet.planning.repository.planningRepository;

import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;


public class ModuleRepository extends JpaRepository<Module, Long > {
    public ModuleRepository( EntityManager entityManager) {
        super(Module.class, entityManager);
    }


    public List<Module> findAll() {
        String jpql = "SELECT m FROM Module m";
        TypedQuery<Module> query = em.createQuery(jpql, Module.class);
        return query.getResultList();
    }

    public Module findByCode(String code) {
        String jpql = "SELECT m FROM Module m WHERE m.code = :code";
        TypedQuery<Module> query = em.createQuery(jpql, Module.class);
        return query.setParameter("code", code).getSingleResult();
    }

}
