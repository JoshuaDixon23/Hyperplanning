package fr.univtln.projet.planning.entity.academic;

import fr.univtln.projet.planning.entity.person.LocalStudent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Promo {
    private final String name;
    private final StudyLevel studyLevel;
    private final UFR ufr; //diamant noir attention

    //private Professor responsible;

    private final Set<LocalStudent> localStudents = new HashSet<>();
    private final Set<Group> groups = new HashSet<>();

    //factory

    private Promo(String name, StudyLevel studyLevel,UFR ufr) {
        this.name=name;
        this.studyLevel = studyLevel;
        this.ufr=ufr;
    }

    static Promo PromoFactory(String name, StudyLevel studyLevel,UFR ufr) {

        //conditions ??
        return new Promo(name, studyLevel,ufr);
    }


    // getter setter

    public String getName(){
        return name;
    }


    public UFR getUfr(){
        return ufr;
    }

    public StudyLevel getStudyLevel() {
        return studyLevel;
    }

    public Set<LocalStudent> getLocalStudent() {
        return localStudents;
    }

    public Set<Group> getGroups() {
        return groups;
    }


    // manage promo

    public void addStudent(LocalStudent s) {
        if (s == null) return; //throw ?
        else {
            localStudents.add(s);
            s.setPromo(this);
        }
    }

    public void removeStudent(LocalStudent s) {
        if (s == null) return; //throw ?
        else {
            localStudents.remove(s);
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

    // equals et hascode


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Promo promo = (Promo) o;
        return Objects.equals(name, promo.name) && studyLevel == promo.studyLevel && Objects.equals(ufr, promo.ufr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, studyLevel, ufr);
    }
}
