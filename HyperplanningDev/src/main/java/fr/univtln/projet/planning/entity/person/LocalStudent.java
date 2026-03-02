package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.academic.Group;
import fr.univtln.projet.planning.academic.Promo;

import java.util.ArrayList;
import java.util.List;

public class LocalStudent extends Student {
    private List<Group> groups;
    private Promo promo;

    public LocalStudent(String fname, String lname) {
        super(fname, lname);
        this.groups = new ArrayList<Group>();
    }

    public static LocalStudent LocalStudentFactory(String fname, String lname, /*Promo promo,*/ String emailPersonal) {
        LocalStudent s =  UserFactory(fname, lname, LocalStudent::new);
        s.setEmailPersonal(emailPersonal.toLowerCase());
        // s.promo = promo;
        return s;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    public void removeGroup(Group group) {
        this.groups.remove(group);
    }
}
