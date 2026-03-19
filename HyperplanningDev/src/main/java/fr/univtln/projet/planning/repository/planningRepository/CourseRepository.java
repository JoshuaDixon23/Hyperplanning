package fr.univtln.projet.planning.repository.planningRepository;


import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class CourseRepository extends JpaRepository<Course, Long >  {



    //constructeur
    protected CourseRepository(Class<Course> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);

    }


    // récuperer le planning d'une promo

    public List<Course> getPlanningByGroup(Group group, Instant beginning, Instant end) {
        String jpql = "SELECT c FROM Group g JOIN g.planning c " +
                "WHERE g = :group " +
                "AND c.startTime BETWEEN :beginning AND :end";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("group", group);
        query.setParameter("beginning", beginning);
        query.setParameter("end", end);
        return query.getResultList();
    }

    // récuperer le planning d'un module
    public List<Course> getPlanningByModule(Module module, Instant beginning, Instant end) {
        String jpql = "SELECT c FROM Module m JOIN m.planning c " +
                "WHERE m = :module " +
                "AND c.startTime BETWEEN :beginning AND :end";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("module", module);
        query.setParameter("beginning", beginning);
        query.setParameter("end", end);
        return query.getResultList();
    }

    // récuperer le planning d'une salle

    public List<Course> getPlanningByRoom(Room room, Instant beginning, Instant end) {
        String jpql = "SELECT c FROM Room r JOIN r.planning c " +
                "WHERE r = :room " +
                "AND c.startTime BETWEEN :beginning AND :end";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("room", room);
        query.setParameter("beginning", beginning);
        query.setParameter("end", end);
        return query.getResultList();
    }

}
