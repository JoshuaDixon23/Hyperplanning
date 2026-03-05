package fr.univtln.projet.planning.entity.academic;
import fr.univtln.projet.planning.entity.person.Student;
import fr.univtln.projet.planning.entity.planning.Planning;
import fr.univtln.projet.planning.entity.planning.Module;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * surement def equals etc dans bcp de choses 
 */
public class Group {

    private GroupType type;
    private final Planning planning;
    private Promo promo;

    private final Set<Module> modules = new HashSet<>();
    private final Set<Student> students = new HashSet<>();

    //factory

    private Group(GroupType type) {
        this.type = type;
        this.planning = new Planning();
    }

    public static Group GroupFactory(GroupType type) {
        //conditions ??
        return new Group(type);
    }

    //setter getter

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public Planning getPlanning() {
        return planning;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Set<Student> getStudents() {
        return students;
    }

    // manage group

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












}
