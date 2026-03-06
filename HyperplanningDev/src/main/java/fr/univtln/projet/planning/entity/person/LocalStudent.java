package fr.univtln.projet.planning.entity.person;
import fr.univtln.projet.planning.entity.academic.Promo;
import fr.univtln.projet.planning.entity.academic.Group;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    // equals et hashCode


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocalStudent that = (LocalStudent) o;
        return Objects.equals(groups, that.groups) && Objects.equals(promo, that.promo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, promo);
    }
}
