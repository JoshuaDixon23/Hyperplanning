package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.person.LocalStudent;
import fr.univtln.projet.planning.entity.academic.Group;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Promo {

    private final int year;
    private final StudyLevel studyLevel;

    private final Set<LocalStudent> students = new HashSet<>();
    private final Set<Group> groups = new HashSet<>();

    //factory

    private Promo(int year, StudyLevel studyLevel) {
        this.year = year;
        this.studyLevel = studyLevel;
    }

    public static Promo PromoFactory(int year, StudyLevel studyLevel) {
        return new Promo(year, studyLevel);
    }


    // getter setter

    public int getYear() {
        return year;
    }

    public StudyLevel getStudyLevel() {
        return studyLevel;
    }

    public Set<LocalStudent> getStudents() {
        return students;
    }

    public Set<Group> getGroups() {
        return groups;
    }



    // manage promo

    public void addStudent(LocalStudent s) {
        if (s == null) return;
        else {
            students.add(s);
            s.setPromo(this); // modifier les package dans localstudent
        }
    }

    public void removeStudent(LocalStudent s) {
        if (s == null) return;
        else {
            students.remove(s);
            s.setPromo(null); // modifier les package dans localstudent
        }
    }


    public void addGroup(Group g) {
        if (g == null) return;
        else  {
            groups.add(g);
            g.setPromo(this); // ajouter dans grp le set promo
        }


    }
    public void removeGroup(Group g) {
        if (g == null) return;
        else  {
            groups.remove(g);
            g.setPromo(null); // ajouter dans grp le set promo
        }

    }








}
