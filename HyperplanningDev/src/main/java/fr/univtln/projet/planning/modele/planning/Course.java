package fr.univtln.projet.planning.modele.planning;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import fr.univtln.projet.planning.modele.person.Professor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Courses" /*,
        uniqueConstraints = @UniqueConstraint(columnNames = {"module", "date", "startTime"} )*/
)
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

    @ManyToMany
    @JoinTable(
            name = "Group_Courses",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "groupId")
    )
    private Set<Group> groups = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "Professor_Courses",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<Professor> professors = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private CourseState state;

    protected Course() {
    }

    private Course(Builder b) {
        this.courseId = b.courseId;
        this.module = b.module;
        this.date = b.date;
        this.startTime = b.startTime;
        this.duration = b.duration;
        this.professors = b.professors;
        this.groups = b.groups;
        this.room = b.room;
        this.courseType = b.courseType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long courseId = null;
        private Module module = null;
        private LocalDate date = null;
        private LocalTime startTime = null;
        private Duration duration = Duration.ZERO;;
        private Set<Professor> professors = new HashSet<>();
        private Set<Group> groups = new HashSet<>();
        private Room room = null;
        private CourseType courseType = null;
        private CourseState state;

        public Builder() {}

        public Builder courseId(Long id) { this.courseId = id; return this; }
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
        public Builder courseState(CourseState s) { this.state = s; return this; }

        public Course build() {
            // Strict Validation
            Objects.requireNonNull(module, "module is required");
            Objects.requireNonNull(date, "date is required");
            Objects.requireNonNull(startTime, "startTime is required");
            Objects.requireNonNull(duration, "duration required");
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
        if (!(o instanceof Course)) return false;
        Course c = (Course) o;
        return courseId != null && courseId.equals(c.courseId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
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

    public void addGroup(Group g) {
        if (g != null && !this.groups.contains(g)) {
            this.groups.add(g);
            g.addCourse(this); 
        }
    }
    public void removeGroup(Group g) {
        if (g != null && this.groups.contains(g)) {
            this.groups.remove(g);
            g.removeCourse(this); 
        }
    }

    public void addProfessor(Professor p) {
        if (p != null && !professors.contains(p)) {
            professors.add(p);
            p.getPlanning().add(this);
        }
    }

    public boolean overlapsWith(Course other) {
        LocalTime start1 = this.startTime;
        LocalTime end1 = start1.plusMinutes(this.duration.toMinutes());

        LocalTime start2 = other.getStartTime();
        LocalTime end2 = start2.plusMinutes(other.getDuration().toMinutes());

        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}