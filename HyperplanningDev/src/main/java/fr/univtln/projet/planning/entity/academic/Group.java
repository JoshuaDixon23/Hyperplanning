package fr.univtln.projet.planning.entity.academic;
import fr.univtln.projet.planning.entity.person.Professor;
import fr.univtln.projet.planning.entity.person.Student;
import fr.univtln.projet.planning.entity.planning.Course;
import fr.univtln.projet.planning.entity.planning.Planning;
import fr.univtln.projet.planning.entity.planning.Module;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * surement def equals etc dans bcp de choses
 */
public class Group {

    private int num;
    private final GroupType type;
    //private final Planning planning; // à enlever et relier a cours plutot
    private Promo promo;

    private final Set<Module> modules = new HashSet<>(); // enlever
    private Set<Course> courses = new HashSet<>();
    private Set<Student> students = new HashSet<>();

    //factory

    private Group(int num, GroupType type) {
        this.type = type;
    }

    public static Group GroupFactory(int num,GroupType type) {
        //conditions ??
        return new Group(num,type);
    }

    //setter getter


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public GroupType getType() {
        return type;
    }


    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // manage group

    public void addCourse(Course c) {
        if (c == null) {
            return; // throw qq chose
        }
        courses.add(c);
    }

    public void removeCourse(Course c) {
        courses.remove(c);
    }

    public void addModule(Module m) {
        if (m == null) {
            return; // throw qq chose
        }
        modules.add(m);
    }

    public void removeModule(Module m) {
        modules.remove(m);
    }


    public void addStudent(Student s) {
        if (s == null) {
            return; // throw qq chose
        }
        else{
            students.add(s);
        }
    }

    public void removeStudent(Student s) {
        students.remove(s);
    }


    // euals and hashCode


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return num == group.num && type == group.type && Objects.equals(promo, group.promo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, type, promo);
    }
}
