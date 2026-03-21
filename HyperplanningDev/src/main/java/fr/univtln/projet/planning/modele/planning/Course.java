package fr.univtln.projet.planning.modele.planning;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.modele.person.Professor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "Course")
@Getter
@Setter
public class Course implements Comparable<Course> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Duration duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseType courseType;

    @ManyToOne
    @JoinColumn(name = "idRoom")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "moduleCode", nullable = false)
    private Module module;

//    @ManyToMany
//    @JoinTable(
//            name = "Group_Course",
//            joinColumns = @JoinColumn(name = "courseId"),
//            inverseJoinColumns = @JoinColumn(name = "groupId")
//    )
//    private Set<Group> groups = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "Professor_Course",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<Professor> professors = new ArrayList<>();

    protected Course() {
    }

    private Course(Builder b) {
        this.module = b.module;
        this.date = b.date;
        this.startTime = b.startTime;
        this.duration = b.duration;
        this.professors = b.professors;
        //this.groups = b.groups;
        this.room = b.room;
        this.courseType = b.courseType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Module module = null;
        private LocalDate date = null;
        private LocalTime startTime = null;
        private Duration duration = Duration.ZERO;;
        private List<Professor> professors = new ArrayList<>();
        private Set<Group> groups = new HashSet<>();
        private Room room = null;
        private CourseType courseType = null;

        public Builder() {}

        public Builder module(Module m) { this.module = m; return this; }
        public Builder date(LocalDate d) { this.date = d; return this; }
        public Builder startTime(LocalTime t) { this.startTime = t; return this; }
        public Builder duration(Duration d) { this.duration = d; return this; }

        // Add professors one by one
        public Builder professor(Professor p) {
            this.professors.add(p);
            return this;
        }

        // Add groups one by one
        public Builder group(Group g) {
            this.groups.add(g);
            return this;
        }

        public Builder room(Room r) { this.room = r; return this; }
        public Builder courseType(CourseType t) { this.courseType = t; return this; }

        public Course build() {
            // Strict Validation
            Objects.requireNonNull(module, "module is required");
            Objects.requireNonNull(date, "date is required");
            Objects.requireNonNull(startTime, "startTime is required");
            Objects.requireNonNull(courseType, "courseType is required");
            if (duration.getSeconds() == 0) {
                throw new IllegalArgumentException("duration must be strictly positive");
            }

            return new Course(this);
        }
    }


    @Override
    public int compareTo(Course c) {
        // Compare by Date first, then by Start Time
        int dateComparison = this.date.compareTo(c.date);
        if (dateComparison != 0) {
            return dateComparison;
        }
        return this.startTime.compareTo(c.startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return duration == course.duration &&
                Objects.equals(date, course.date) &&
                Objects.equals(startTime, course.startTime) &&
                Objects.equals(module, course.module) &&
                Objects.equals(room, course.room) &&
                Objects.equals(courseType, course.courseType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, duration, module, room, courseType);
    }


    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", date=" + date +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", courseType=" + courseType +
                ", room=" + room +
                ", module=" + module +
                /*", groups=" + groups + */
                ", professors=" + professors +
                '}';
    }
}