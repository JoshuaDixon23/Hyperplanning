package fr.univtln.projet.planning.repository.planningRepository;


import java.time.LocalDate;
import java.util.List;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.JpaRepository;
import jakarta.persistence.EntityManager;

public class CourseRepository extends JpaRepository<Course, Long> {

    public CourseRepository(EntityManager entityManager) {
        super(Course.class, entityManager);
    }

    // ----------------- Find All -----------------
    public List<Course> findAll() {
        String jpql = "SELECT c FROM Course c ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class).getResultList();
    }

    // find for empty room

    public List<Course> findByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date nulle");
        }

        String jpql = "SELECT c FROM Course c " +
                "WHERE c.date = :date " +
                "ORDER BY c.startTime";

        return em.createQuery(jpql, Course.class)
                .setParameter("date", date)
                .getResultList();
    }

    // ----------------- Planning by Group -----------------
    public List<Course> findByGroupAndPeriod(Group group, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT c FROM Course c JOIN c.groups g " +
                "WHERE g = :group AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("group", group)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public List<Course> findByGroupIdAndPeriod(Long groupId, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT c FROM Course c JOIN c.groups g " +
                "WHERE g.groupId = :groupId AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("groupId", groupId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    // ----------------- Planning by Module -----------------
    public List<Course> findByModuleAndPeriod(Module module, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT c FROM Course c " +
                "WHERE c.module = :module AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("module", module)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public List<Course> findByModuleCodeAndPeriod(String code, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT c FROM Course c " +
                "WHERE c.module.code = :code AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("code", code)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    // ----------------- Planning by Room -----------------
    public List<Course> findByRoomIdAndPeriod(Long roomId, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT DISTINCT c FROM Course c " +
                "WHERE c.room.idRoom = :roomId AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("roomId", roomId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    // ----------------- Planning by Professor -----------------
    public List<Course> findByProfessorAndPeriod(Professor professor, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT c FROM Course c JOIN c.professors p " +
                "WHERE p = :professor AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("professor", professor)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public List<Course> findByProfessorIdAndPeriod(Long professorId, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT c FROM Course c JOIN c.professors p " +
                "WHERE p.id = :professorId AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("professorId", professorId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    // ----------------- Planning by Student -----------------
    public List<Course> findByStudentIdAndPeriod(Long studentId, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT DISTINCT c FROM Course c " +
                "JOIN c.groups g JOIN g.localStudents s " +
                "WHERE s.userId = :studentId AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("studentId", studentId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public List<Course> findByStudentEmailUnivAndPeriod(String emailUniv, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT DISTINCT c FROM Course c " +
                "JOIN c.groups g JOIN g.localStudents s " +
                "WHERE s.emailUniv = :emailUniv AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("emailUniv", emailUniv)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    // ----------------- Planning by Promo -----------------
    public List<Course> findByPromoIdAndPeriod(Long promoId, LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        String jpql = "SELECT DISTINCT c FROM Course c " +
                "JOIN c.groups g " +
                "WHERE g.promo.promoId = :promoId AND c.date BETWEEN :start AND :end " +
                "ORDER BY c.date, c.startTime";
        return em.createQuery(jpql, Course.class)
                .setParameter("promoId", promoId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    // ----------------- Validations -----------------
    private void validatePeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates nulles");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Période invalide");
        }
    }
}