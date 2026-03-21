package fr.univtln.projet.planning.entity.person;
import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.entity.academic.GroupEntity;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalStudentEntity extends StudentEntity {
    private List<GroupEntity> groups;
    @Setter
    private PromoEntity promo;

    public LocalStudentEntity(String fname, String lname) {
        super(fname, lname);
        this.groups = new ArrayList<GroupEntity>();
    }

    public LocalStudentEntity(String firstName, String lastName, String emailUniv, String emailPersonal) {
        super(firstName, lastName);
        this.emailUniv = emailUniv;
        this.emailPersonal = emailPersonal;
        this.groups = new ArrayList<GroupEntity>();
    }

    public static LocalStudentEntity LocalStudentFactory(String fname, String lname, /*Promo promo,*/ String emailPersonal) {
        LocalStudentEntity s =  UserFactory(fname, lname, LocalStudentEntity::new);
        s.setEmailPersonal(emailPersonal.toLowerCase());
        // s.promo = promo;
        return s;
    }

    public void addGroup(GroupEntity group) {
        this.groups.add(group);
    }

    public void removeGroup(GroupEntity group) {
        this.groups.remove(group);
    }


    // equals et hashCode


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocalStudentEntity that = (LocalStudentEntity) o;
        return Objects.equals(groups, that.groups) && Objects.equals(promo, that.promo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, promo);
    }
}
