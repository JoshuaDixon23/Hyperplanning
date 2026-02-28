package fr.univtln.projet.planning.entity.planning;

import java.util.Collection;
import java.util.TreeSet;

public class Planning {
    Collection<Course> courses;

    public Planning(Collection<Course> courses) {
        this.courses = courses;
    }

    public Planning() {
        this.courses = new TreeSet<>(); // to order courses by their startTime (so the date as well)
    }

    public Collection<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
    }
}
