package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.person.LocalStudent;
import fr.univtln.projet.planning.entity.academic.Group;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Promo {

    private final int year;
    private final StudyLevel studyLevel;
    private  UFR ufr; //diaman noir attention

    //private Professor responsible;

    private final Set<LocalStudent> students = new HashSet<>();
    private final Set<Group> groups = new HashSet<>();

    //factory

    private Promo(int year, StudyLevel studyLevel) {
        this.year = year;
        this.studyLevel = studyLevel;
    }

    public static Promo PromoFactory(int year, StudyLevel studyLevel) {

        //conditions ??
        return new Promo(year, studyLevel);
    }


    // getter setter

    public int getYear() {
        return year;
    }

    public UFR getUfr(){
        return ufr;
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

    public void setUfr(UFR u){
        this.ufr=u;
    }




    // manage promo

    public void addStudent(LocalStudent s) {
        if (s == null) return; //throw ?
        else {
            students.add(s);
            s.setPromo(this);
        }
    }

    public void removeStudent(LocalStudent s) {
        if (s == null) return; //throw ?
        else {
            students.remove(s);
            s.setPromo(null);
        }
    }


    public void addGroup(Group g) {
        if (g == null) return; //throw ?
        else  {
            groups.add(g);
            g.setPromo(this); // ajouter dans grp le set promo
        }


    }
    public void removeGroup(Group g) {
        if (g == null) return; //throw??
        else  {
            groups.remove(g);
            g.setPromo(null); // ajouter dans grp le set promo
        }

    }








}
