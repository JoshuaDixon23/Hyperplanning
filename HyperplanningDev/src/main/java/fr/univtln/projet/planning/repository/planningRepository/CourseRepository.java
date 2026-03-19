package fr.univtln.projet.planning.repository.planningRepository;


import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class CourseRepository extends JpaRepository<Course, Long >  {



    //constructeur
    protected CourseRepository(Class<Course> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);

    }


    // récuperer le planning d'un group par id ou par group

    public List<Course> getPlanningByGroup(Group group, LocalDate beginningDate, LocalDate endDate) {
        String jpql = "SELECT c FROM Group g JOIN g.planning c " +
                "WHERE g = :group " +
                "AND c.date BETWEEN :beginningDate AND :endDate"; // on peut modifier pour ne pas inclure les chevauchement

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("group", group);
        query.setParameter("beginningDate", beginningDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<Course> getPlanningByGroup(Long groupId, LocalDate beginningDate, LocalDate endDate) {
        String jpql = "SELECT c FROM Group g JOIN g.planning c " +
                "WHERE g.groupId = :groupId " +
                "AND c.date BETWEEN :beginningDate AND :endDate";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("groupId", groupId);
        query.setParameter("beginningDate", beginningDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    // récuperer le planning d'un module par module et par id (code)
    public List<Course> getPlanningByModule(Module module, LocalDate beginningDate, LocalDate endDate) {
        String jpql = "SELECT c FROM Module m JOIN m.planning c " +
                "WHERE m = :module " +
                "AND c.date BETWEEN :beginningDate AND :endDate";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("module", module);
        query.setParameter("beginningDate", beginningDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<Course> getPlanningByModule(String code, LocalDate beginningDate, LocalDate endDate) {
        String jpql = "SELECT c FROM Module m JOIN m.planning c " +
                "WHERE m.code = :code " +
                "AND c.date BETWEEN :beginningDate AND :endDate";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("code", code);
        query.setParameter("beginningDate", beginningDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    // récuperer le planning d'une salle par salle et par id

    public List<Course> getPlanningByRoom(Long idRoom, LocalDate beginningDate, LocalDate endDate) {
        String jpql = "SELECT c FROM Room r JOIN r.planning c " +
                "WHERE r.idRoom = :idRoom " +
                "AND c.date BETWEEN :beginningDate AND :endDate";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("idRoom", idRoom);
        query.setParameter("beginningDate", beginningDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

}
