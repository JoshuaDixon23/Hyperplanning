package fr.univtln.projet.planning.modele.person;

import fr.univtln.projet.planning.modele.academic.Group; 
import fr.univtln.projet.planning.modele.academic.Promo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "LocalStudent")
@Getter
@Setter
public class LocalStudent extends Student {


    @ManyToOne
    @JoinColumn(name = "promoId")
    private Promo promo;

    // "localStudents" correspond au nom exact de l'attribut dans ta classe Group
    @ManyToMany(mappedBy = "localStudents")
    private List<Group> groups = new ArrayList<>();


    protected LocalStudent() {
        super();
    }

    private LocalStudent(String firstName, String lastName) {
        super(firstName, lastName);
    }


    public static LocalStudent LocalStudentFactory(String fname, String lname, String emailPersonal) {
        LocalStudent s = UserFactory(fname, lname, LocalStudent::new);
        
        if (emailPersonal != null) {
            s.setEmailPersonal(emailPersonal.toLowerCase());
        }
        
        return s;
    }

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