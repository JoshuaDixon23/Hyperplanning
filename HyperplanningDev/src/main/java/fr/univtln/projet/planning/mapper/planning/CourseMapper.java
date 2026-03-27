package fr.univtln.projet.planning.mapper.planning;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.planning.Course;

public class CourseMapper {

    public static CourseEntity toDomain(Course c) {
        if (c == null) return null;
        CourseEntity.Builder builder = CourseEntity.builder()
                .module(moduleService.toDomain(c.getModule()))
                .date(c.getDate())
                .startTime(c.getStartTime())
                .duration(c.getDuration())
                .room(roomService.toDomain(c.getRoom()))
                .courseType(c.getCourseType());

        if (c.getProfessors() != null) {
            c.getProfessors().forEach(prof -> builder.professor(professorService.toDomain(prof)));
        }

        return builder.build();
    }

    public static Course toJpa(CourseEntity c) {
        if (c == null) return null;
        Course.Builder builder = Course.builder()
                .module(moduleService.toJpa(c.getModule()))
                .date(c.getDate())
                .startTime(c.getStartTime())
                .duration(c.getDuration())
                .room(roomService.toJpa(c.getRoom()))
                .courseType(c.getCourseType());

        if (c.getProfessors() != null) {
            c.getProfessors().forEach(prof -> builder.professor(professorService.toJpa(prof)));
        }

        return builder.build();
    }
}
