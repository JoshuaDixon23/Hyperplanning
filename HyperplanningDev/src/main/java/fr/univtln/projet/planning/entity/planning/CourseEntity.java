package fr.univtln.projet.planning.entity.planning;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import fr.univtln.projet.planning.modele.planning.CourseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CourseEntity implements Comparable<CourseEntity>{
    private final ModuleEntity module;

    private LocalDate date;
    private LocalTime startTime;
    private Duration duration;
    private Set<ProfessorEntity> professors;
    private RoomEntity room;
    private CourseType courseType; // may be final
    // private State state; (State has to be an Enum of "annulated", "in progress"... respectively to state diagram
    private Set<GroupEntity> groups = new HashSet<>();

    public CourseEntity(Builder b) {
        module = b.module;
        date = b.date;
        startTime = b.startTime;
        duration = b.duration;
        professors = b.professors;
        room = b.room;
        courseType = b.courseType;
    }

    public ModuleEntity module() { return module; }

    public LocalDate date() { return date; }

    public LocalTime startTime() { return startTime; }

    public Duration duration() { return duration; }

    public Set<ProfessorEntity> professors() { return professors; }

    public RoomEntity room() { return room; }

    public CourseType courseType() { return courseType; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private ModuleEntity module = null;
        private LocalDate date = null;
        private LocalTime startTime = null;
        private Duration duration = null;
        private Set<ProfessorEntity> professors = new HashSet<>();
        private RoomEntity room = null;
        private CourseType courseType = null;

        public Builder() {
        }

        public Builder module(ModuleEntity m) { this.module = m; return this; }

        public Builder date(LocalDate date) { this.date = date; return this; }

        public Builder startTime(LocalTime t) { this.startTime = t; return this; }

        public Builder duration(Duration d) { this.duration = d; return this; }

        // name in singular in order to add professors one by one
        public Builder professor(ProfessorEntity p) {
            this.professors.add(p);
            return this;
        }

        public Builder room(RoomEntity r) { this.room = r; return this; }

        public Builder courseType(CourseType t) { this.courseType = t; return this; }

        public CourseEntity build() {
            Objects.requireNonNull(module, "module required");
            Objects.requireNonNull(date, "date required");
            Objects.requireNonNull(startTime, "startTime required");
            Objects.requireNonNull(duration, "duration required");
            Objects.requireNonNull(courseType, "courseType required");
            //Objects.requireNonNull(professors, "at least one professor required");

            return new CourseEntity(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseEntity course = (CourseEntity) o;
        return Objects.equals(module, course.module)
                && Objects.equals(date, course.date)
                && Objects.equals(startTime, course.startTime)
                && Objects.equals(duration, course.duration)
                && Objects.equals(professors, course.professors)
                && Objects.equals(room, course.room)
                && courseType == course.courseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(module, date, startTime, duration, professors, room, courseType);
    }
    @Override
    public int compareTo(CourseEntity c) {
        int cmp = this.date.compareTo(c.date);
        if (cmp != 0) return cmp;
        return this.startTime.compareTo(c.startTime);
    }


    @Override
    public String toString() {
        return "CourseEntity{" +
                "module=" + module +
                ", date=" + date +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", professors=" + professors +
                ", room=" + room +
                ", courseType=" + courseType +
                '}';
    }

    public void addGroup(GroupEntity g) {
        if (g != null && !groups.contains(g)) {
            groups.add(g);
            g.addCourse(this); // sync bidirectionnelle
        }
    }

    public void removeGroup(GroupEntity g) {
        if (g != null && groups.contains(g)) {
            groups.remove(g);
            g.removeCourse(this);
        }
    }

    public void addProfessor(ProfessorEntity p) {
        if (p != null && !professors.contains(p)) {
            professors.add(p);
            p.addCourse(this);
        }
    }

    public void removeProfessor(ProfessorEntity p) {
        if (p != null && professors.contains(p)) {
            professors.remove(p);
            p.removeCourse(this);
        }
    }
}