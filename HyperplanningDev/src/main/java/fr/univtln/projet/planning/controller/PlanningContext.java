package fr.univtln.projet.planning.controller;

public class PlanningContext {

    private final PlanningViewType type;
    private final Long id;

    public PlanningContext(PlanningViewType type, Long id) {
        this.type = type;
        this.id = id;
    }

    public PlanningViewType getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public static PlanningContext forStudent(Long studentId) {
        return new PlanningContext(PlanningViewType.STUDENT, studentId);
    }

    public static PlanningContext forProfessor(Long professorId) {
        return new PlanningContext(PlanningViewType.PROFESSOR, professorId);
    }

    public static PlanningContext forGroup(Long groupId) {
        return new PlanningContext(PlanningViewType.GROUP, groupId);
    }

    public static PlanningContext forPromo(Long promoId) {
        return new PlanningContext(PlanningViewType.PROMO, promoId);
    }

    public static PlanningContext forRoom(Long roomId) {
        return new PlanningContext(PlanningViewType.ROOM, roomId);
    }
}