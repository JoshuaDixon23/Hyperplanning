package fr.univtln.projet.planning.entity.academic;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.academic.GroupType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

/**
 * surement def equals etc dans bcp de choses
 */
public class GroupEntity {
    private final int num;
    private final GroupType type;
    //private final Planning planning; // à enlever et relier a cours plutôt

    private PromoEntity promo;

//    private Set<ModuleEntity> modules = new HashSet<>();

    private Set<CourseEntity> planning = new TreeSet<>();
    private Set<LocalStudentEntity> localStudents = new HashSet<>();

    //factory

    private GroupEntity(int num, GroupType type) {
        this.num = num;
        this.type = type;
    }

    public static GroupEntity GroupFactory(int num,GroupType type) {
        //conditions ??
        return new GroupEntity(num,type);
    }

    //setter getter


    public Set<LocalStudentEntity> getStudents() {
        return localStudents;
    }

    // manage group

    public void addCourse(CourseEntity c) {
        if (c == null) {
            return; // throw qq chose
        }
        planning.add(c);
    }

    public void removeCourse(CourseEntity c) {
        planning.remove(c);
    }

    /*
    public void addModule(ModuleEntity m) {
        if (m == null) {
            return; // throw qq chose
        }
        modules.add(m);
    }

    public void removeModule(ModuleEntity m) {
        modules.remove(m);
    }

    */

    public void addLocalStudent(LocalStudentEntity s) {
        if (s != null && !localStudents.contains(s)) {
            localStudents.add(s);
            s.addGroup(this); // synchronisation côté propriétaire
        }
    }

    public void removeLocalStudent(LocalStudentEntity s) {
        if (s != null && localStudents.contains(s)) {
            localStudents.remove(s);
            s.removeGroup(this);
        }
    }

    // equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GroupEntity group = (GroupEntity) o;
        return num == group.num && type == group.type && Objects.equals(promo, group.promo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, type, promo);
    }

    @Override
    public String toString() {
        return "GroupEntity{" +
                "num=" + num +
                ", type=" + type +
                ", promo=" + (promo != null ? promo.getName() + "-" + promo.getYear() + "-" + promo.getStudyLevel(): "null") +
                '}';
    }
}
