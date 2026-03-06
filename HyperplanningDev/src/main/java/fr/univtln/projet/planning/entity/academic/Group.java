package fr.univtln.projet.planning.entity.academic;
import fr.univtln.projet.planning.entity.person.LocalStudent;
import fr.univtln.projet.planning.entity.person.Student;
import fr.univtln.projet.planning.entity.planning.Course;
import fr.univtln.projet.planning.entity.planning.Module;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * surement def equals etc dans bcp de choses
 */
public class Group {

    private final int num;
    private final GroupType type;
    //private final Planning planning; // à enlever et relier a cours plutôt
    private Promo promo;

    private final Set<Module> modules = new HashSet<>(); // enlever
    private Set<Course> planning = new TreeSet<>();
    private Set<LocalStudent> localStudents = new HashSet<>();

    //factory

    private Group(int num, GroupType type) {
        this.num = num;
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


    public GroupType getType() {
        return type;
    }


    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public Set<LocalStudent> getStudents() {
        return localStudents;
    }

    public Set<Course> getPlanning() {
        return planning;
    }

    public void setPlanning(Set<Course> planning) {
        this.planning = planning;
    }

    // manage group

    public void addCourse(Course c) {
        if (c == null) {
            return; // throw qq chose
        }
        planning.add(c);
    }

    public void removeCourse(Course c) {
        planning.remove(c);
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


    public void addLocalStudent(LocalStudent s) {
        if (s == null) {
            return; // throw qq chose
        }
        else{
            localStudents.add(s);
        }
    }

    public void removeLocalStudent(LocalStudent s) {
        localStudents.remove(s);
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
