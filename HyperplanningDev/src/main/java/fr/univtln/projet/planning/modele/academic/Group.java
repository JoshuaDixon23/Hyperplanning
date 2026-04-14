package fr.univtln.projet.planning.modele.academic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.modele.planning.Module;
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
@Table(name = "Groups") // doubtable name of table
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
    @ManyToMany(mappedBy = "groups")
    private Set<LocalStudent> localStudents = new HashSet<>();

    public void addLocalStudent(LocalStudent s) {
        if (s != null && !localStudents.contains(s)) {
            localStudents.add(s);
            s.getGroups().add(this); // synchronisation côté propriétaire
        }
    }

    public void removeLocalStudent(LocalStudent s) {
        if (s != null && localStudents.contains(s)) {
            localStudents.remove(s);
            s.getGroups().remove(this);
        }
    }

    @ManyToMany
    @JoinTable(
            name = "Group_Modules",
            joinColumns = @JoinColumn(name = "groupId"),
            inverseJoinColumns = @JoinColumn(name = "moduleCode")
    )
    private Set<Module> modules = new HashSet<>();

    public void addModule(Module m) {
        if (m != null) {
            modules.add(m);
            m.getGroups().add(this);
        }
    }

    public void removeModule(Module m) {
        if (m != null) {
            modules.remove(m);
            m.getGroups().remove(this);
        }
    }

    public Group() {
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
        if (c != null && !this.planning.contains(c)) { 
            this.planning.add(c);
            c.addGroup(this); 
        }
    }

    public void removeCourse(Course c) {
        if (c != null && this.planning.contains(c)) {
            this.planning.remove(c);
            c.removeGroup(this); 
        }
    }

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