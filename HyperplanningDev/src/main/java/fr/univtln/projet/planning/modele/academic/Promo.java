package fr.univtln.projet.planning.modele.academic;

import fr.univtln.projet.planning.modele.person.LocalStudent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Promo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "studyLevel", "year"})
)
@Getter
@Setter
public class Promo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promoId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int year; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyLevel studyLevel;

    @ManyToOne
    @JoinColumn(name = "idUFR", nullable = false)
    private UFR ufr;

    //@OneToMany(mappedBy = "promo", cascade = CascadeType.ALL)
    //private Set<LocalStudent> localStudents = new HashSet<>();

    //@OneToMany(mappedBy = "promo", cascade = CascadeType.ALL, orphanRemoval = true)
    //private Set<Group> groups = new HashSet<>();

    protected Promo() {
    }

    private Promo(String name, int year, StudyLevel studyLevel, UFR ufr) {
        this.name = name;
        this.year = year;
        this.studyLevel = studyLevel;
        this.ufr = ufr;
    }

    public static Promo PromoFactory(String name, int year, StudyLevel studyLevel, UFR ufr) {
        Objects.requireNonNull(name, "Promo name cannot be null");
        Objects.requireNonNull(studyLevel, "Study level cannot be null");
        Objects.requireNonNull(ufr, "Promo must be attached to an UFR");
        
        return new Promo(name, year, studyLevel, ufr);
    }

    /*
    public Set<LocalStudent> getLocalStudents() {
        return Collections.unmodifiableSet(localStudents);
    }

    public void addStudent(LocalStudent s) {
        if (s != null) {
            localStudents.add(s);
            s.setPromo(this); 
        }
    }

    public void removeStudent(LocalStudent s) {
        if (s != null) {
            localStudents.remove(s);
            s.setPromo(null); 
        }
    }

    public Set<Group> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    public void addGroup(Group g) {
        if (g != null) {
            groups.add(g);
            g.setPromo(this);
        }
    }

    public void removeGroup(Group g) {
        if (g != null) {
            groups.remove(g);
            g.setPromo(null);
        }
    }

     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promo promo = (Promo) o;
        // Une promo est unique par son nom, son année, son niveau et son UFR
        return year == promo.year && 
               studyLevel == promo.studyLevel && 
               Objects.equals(name, promo.name) && 
               Objects.equals(ufr, promo.ufr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, studyLevel, ufr);
    }
}