package fr.univtln.projet.planning.repository.planningRepository;


import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;


public class CourseRepository extends JpaRepository<Course, Long >  {

    //constructeur
    public CourseRepository(EntityManager entityManager) {
        super(Course.class, entityManager);

    }

    //@Override
    public List<Course> findAll() {
        String jpql = "SELECT g FROM Course g";
        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        return query.getResultList();
    }


    // récuperer le planning d'un group par id ou par group

    public List<Course> getPlanningByGroup(Group group, LocalDate beginningDate, LocalDate endDate) {
        validatePeriod(beginningDate,endDate);
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
        validatePeriod(beginningDate,endDate);
        String jpql = "SELECT c FROM Group g JOIN g.planning c " +
                "WHERE g.groupId = :groupId " +
                "AND c.date BETWEEN :beginningDate AND :endDate";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("groupId", groupId);
        query.setParameter("beginningDate", beginningDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    /*
    // récuperer le planning d'un module par module et par id (code)
    public List<Course> getPlanningByModule(Module module, LocalDate beginningDate, LocalDate endDate) {
        validatePeriod(beginningDate,endDate);
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
        validatePeriod(beginningDate,endDate);
        String jpql = "SELECT c FROM Module m JOIN m.planning c " +
                "WHERE m.code = :code " +
                "AND c.date BETWEEN :beginningDate AND :endDate";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("code", code);
        query.setParameter("beginningDate", beginningDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

     */

    // récuperer le planning d'une salle par salle et par id

    /*
    public List<Course> getPlanningByRoom(Long idRoom, LocalDate beginningDate, LocalDate endDate) {

        validatePeriod(beginningDate,endDate);
        String jpql = "SELECT c FROM Room r JOIN r.planning c " +
                "WHERE r.idRoom = :idRoom " +
                "AND c.date BETWEEN :beginningDate AND :endDate";

        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("idRoom", idRoom);
        query.setParameter("beginningDate", beginningDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

     */

    private void validatePeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates nulles");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Période invalide");
        }
    }
}