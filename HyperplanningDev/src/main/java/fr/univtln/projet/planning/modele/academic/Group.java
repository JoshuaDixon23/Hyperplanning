package fr.univtln.projet.planning.modele.academic;

import java.util.*;

import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.modele.planning.Course;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PlanningGroup")
@Setter
@Getter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private int num;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupType type; 

    @ManyToOne
    @JoinColumn(name = "promoId")
    private Promo promo;

    @ManyToMany(mappedBy = "groups")
    private Set<Course> planning = new HashSet<>();

    // @ManyToMany car un étudiant est dans 1 groupe CM, 1 groupe TD et 1 groupe TP.
//    @ManyToMany
//    @JoinTable(
//        name = "Group_Student",
//        joinColumns = @JoinColumn(name = "groupId"),
//        inverseJoinColumns = @JoinColumn(name = "studentId")
//    )
//    private Set<LocalStudent> localStudents = new HashSet<>();

    protected Group() {
    }

    private Group(int num, GroupType type) {
        this.num = num;
        this.type = type;
    }


    public static Group GroupFactory(int num, GroupType type) {
        Objects.requireNonNull(type, "Group type cannot be null");
        return new Group(num, type);
    }

    public Set<Course> getPlanning() {
        return Collections.unmodifiableSet(planning);
    }

    public void addCourse(Course c) {
        if (c != null) {
            planning.add(c);
            c.getGroups().add(this);
        }
    }

    public void removeCourse(Course c) {
        if (c != null) {
            planning.remove(c);
            c.getGroups().remove(this);
        }
    }

    /*
    public Set<LocalStudent> getStudents() {
        return Collections.unmodifiableSet(localStudents);
    }

    public void addLocalStudent(LocalStudent s) {
        if (s != null) {
            localStudents.add(s);
        }
    }

    public void removeLocalStudent(LocalStudent s) {
        if (s != null) {
            localStudents.remove(s);
        }
    }
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        // Un groupe est unique par son numéro, son type ET sa promo (Ex: TP1 de la L3 n'est pas le TP1 de la L2)
        return num == group.num && type == group.type && Objects.equals(promo, group.promo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, type, promo);
    }
}