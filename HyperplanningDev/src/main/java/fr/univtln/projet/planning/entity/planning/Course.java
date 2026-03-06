package fr.univtln.projet.planning.entity.planning;

import fr.univtln.projet.planning.entity.person.Professor;
import fr.univtln.projet.planning.entity.infrastructure.Room;

import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Comparable<Course>{
    private final Module module;
    private Instant startTime;
    private Duration duration;
    private List<Professor> professors;
    private Room room;
    private CourseType courseType; // may be final
    // private State state; (State has to be an Enum of "annulated", "in progress"... respectively to state diagram

    private Course(Builder b) {
        module = b.module;
        startTime = b.startTime;
        duration = b.duration;
        professors = b.professors;
        room = b.room;
        courseType = b.courseType;
    }

    public Module module() { return module; }

    public Instant startTime() { return startTime; }

    public Duration duration() { return duration; }

    public List<Professor> professors() { return professors; }

    public Room room() { return room; }

    public CourseType courseType() { return courseType; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Module module = null;
        private Instant startTime = null;
        private Duration duration = null;
        private List<Professor> professors = null;
        private Room room = null;
        private CourseType courseType = null;

        public Builder() {
        }

        public Builder module(Module m) { this.module = m; return this; }

        public Builder startTime(Instant t) { this.startTime = t; return this; }

        public Builder duration(Duration d) { this.duration = d; return this; }

        // name in singular in order to add professors one by one
        public Builder professor(Professor p) {
            if (professors == null) {
                professors = new ArrayList<>();
            }
            this.professors.add(p);
            return this;
        }

        public Builder room(Room r) { this.room = r; return this; }

        public Builder courseType(CourseType t) { this.courseType = t; return this; }

        public Course build() {
            Objects.requireNonNull(module, "module required");
            Objects.requireNonNull(startTime, "startTime required");
            Objects.requireNonNull(duration, "duration required");
            Objects.requireNonNull(courseType, "courseType required");
            //Objects.requireNonNull(professors, "at least one professor required");

            return new Course(this);
        }
    }

    public Module getModule() {
        return module;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public Room getRoom() {
        return room;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(module, course.module) && Objects.equals(startTime, course.startTime) && Objects.equals(duration, course.duration) && Objects.equals(professors, course.professors) && Objects.equals(room, course.room) && courseType == course.courseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(module, startTime, duration, professors, room, courseType);
    }

    @Override
    public int compareTo(Course c) {
        return this.startTime.compareTo(c.startTime);
    }
}
