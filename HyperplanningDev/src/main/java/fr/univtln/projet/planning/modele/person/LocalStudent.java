package fr.univtln.projet.planning.modele.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.academic.Promo;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "LocalStudents")
@Getter
@Setter
public class LocalStudent extends Student {

    @ManyToOne
    @JoinColumn(name = "promoId")
    private Promo promo;

    // "localStudents" correspond au nom exact de l'attribut dans ta classe Group
    @ManyToMany
    @JoinTable(
            name = "Student_Groups",
            joinColumns = @JoinColumn(name = "idStudent"),
            inverseJoinColumns = @JoinColumn(name = "idGroup")
    )
    private List<Group> groups = new ArrayList<>();


    protected LocalStudent() {
        super();
    }

    public LocalStudent(String firstName, String lastName, String emailUniv, String emailPersonal, Promo promo) {
        super(firstName, lastName, emailUniv, emailPersonal);
        this.promo = promo;
    }

    /*
    public static LocalStudent LocalStudentFactory(String fname, String lname, String emailPersonal) {
        LocalStudent s = UserFactory(fname, lname, LocalStudent::new);
        
        if (emailPersonal != null) {
            s.setEmailPersonal(emailPersonal.toLowerCase());
        }
        
        return s;
    }
     */

    public List<Group> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    public void addGroup(Group group) {
        if (group != null) {
            this.groups.add(group);
            group.getLocalStudents().add(this);
        }
    }

    public void removeGroup(Group group) {
        if (group != null) {
            this.groups.remove(group);
            group.getLocalStudents().remove(this);
        }
    }
}