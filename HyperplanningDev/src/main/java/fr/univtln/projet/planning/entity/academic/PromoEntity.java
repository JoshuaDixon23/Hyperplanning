package fr.univtln.projet.planning.entity.academic;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.modele.academic.StudyLevel;

public class PromoEntity {
    private final String name;
    private final StudyLevel studyLevel;
    private final UFREntity ufr; //diamant noir attention

    //private Professor responsible;

    private final Set<LocalStudentEntity> localStudents = new HashSet<>();
    private final Set<GroupEntity> groups = new HashSet<>();

    //factory

    private PromoEntity(String name, StudyLevel studyLevel,UFREntity ufr) {
        this.name=name;
        this.studyLevel = studyLevel;
        this.ufr=ufr;
    }

    public static PromoEntity PromoFactory(String name, StudyLevel studyLevel,UFREntity ufr) {

        //conditions ??
        return new PromoEntity(name, studyLevel,ufr);
    }


    // getter setter

    public String getName(){
        return name;
    }


    public UFREntity getUfr(){
        return ufr;
    }

    public StudyLevel getStudyLevel() {
        return studyLevel;
    }

    public Set<LocalStudentEntity> getLocalStudent() {
        return localStudents;
    }

    public Set<GroupEntity> getGroups() {
        return groups;
    }


    // manage promo

    public void addStudent(LocalStudentEntity s) {
        if (s == null) return; //throw ?
        else {
            localStudents.add(s);
            s.setPromo(this);
        }
    }

    public void removeStudent(LocalStudentEntity s) {
        if (s == null) return; //throw ?
        else {
            localStudents.remove(s);
            s.setPromo(null);
        }
    }


    public void addGroup(GroupEntity g) {
        if (g == null) return; //throw ?
        else  {
            groups.add(g);
            g.setPromo(this); // ajouter dans grp le set promo
        }


    }
    public void removeGroup(GroupEntity g) {
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
        PromoEntity promo = (PromoEntity) o;
        return Objects.equals(name, promo.name) && studyLevel == promo.studyLevel && Objects.equals(ufr, promo.ufr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, studyLevel, ufr);
    }
}
