package fr.univtln.projet.planning.mapper.planning;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.mapper.academic.GroupMapper;
import fr.univtln.projet.planning.mapper.infrastracture.RoomMapper;
import fr.univtln.projet.planning.mapper.person.ProfessorMapper;
import fr.univtln.projet.planning.modele.planning.Course;

public class CourseMapper {

    public static CourseEntity toDomain(Course c) {
        if (c == null) return null;
        CourseEntity.Builder builder = CourseEntity.builder()
                .module(ModuleMapper.toDomain(c.getModule()))
                .courseId(c.getCourseId())
                .date(c.getDate())
                .startTime(c.getStartTime())
                .duration(c.getDuration())
                .room(RoomMapper.toDomain(c.getRoom()))
                .courseType(c.getCourseType());

        if (c.getProfessors() != null) {
            c.getProfessors().forEach(prof -> builder.professor(ProfessorMapper.toDomain(prof)));
        }

        return builder.build();
    }

    public static Course toJpa(CourseEntity c) {
        if (c == null) return null;
        Course.Builder builder = Course.builder()
                .module(ModuleMapper.toJpa(c.getModule()))
                .date(c.getDate())
                .startTime(c.getStartTime())
                .duration(c.getDuration())
                .room(RoomMapper.toJpa(c.getRoom()))
                .courseType(c.getCourseType());

        if (c.getRoom() != null) {
            builder.room(RoomMapper.toJpa(c.getRoom()));
        }

        if (c.getProfessors() != null) {
            c.getProfessors().forEach(prof -> builder.professor(ProfessorMapper.toJpa(prof)));
        }
        if (c.getGroups() != null) {
            c.getGroups().forEach(group -> builder.group(GroupMapper.toJpa(group)));
        }

        Course jpa = builder.build();

        if (c.getCourseId() != null) jpa.setCourseId(c.getCourseId());

        return jpa;
    }
}
