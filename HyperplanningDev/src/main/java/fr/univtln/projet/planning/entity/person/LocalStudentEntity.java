package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.international.BasketEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Setter
@Getter

public class LocalStudentEntity extends StudentEntity {
    private List<GroupEntity> groups;
    private PromoEntity promo;

    public LocalStudentEntity(String fname, String lname, String emailUniv, String emailPersonal, PromoEntity promo) {
        super(fname, lname);
        this.emailUniv = emailUniv;
        this.emailPersonal = emailPersonal;
        this.promo = promo;
        this.groups = new ArrayList<GroupEntity>();
    }

    public LocalStudentEntity(String firstName, String lastName, String emailUniv, String emailPersonal) {
        super(firstName, lastName);
        this.emailUniv = emailUniv;
        this.emailPersonal = emailPersonal;
        this.groups = new ArrayList<GroupEntity>();
    }

    private LocalStudentEntity(String fname, String lname){
        super(fname, lname);
    }

    public static LocalStudentEntity LocalStudentFactory(String fname, String lname, String emailPersonal) {
        LocalStudentEntity s =  UserFactory(fname, lname, LocalStudentEntity::new);
        s.setEmailPersonal(emailPersonal.toLowerCase());
        // s.promo = promo;
        return s;
    }

    public void addGroup(GroupEntity g) {
        if (g != null && !groups.contains(g)) {
            groups.add(g);
            g.addLocalStudent(this); // synchronisation côté inverse
        }
    }

    public void removeGroup(GroupEntity g) {
        if (g != null && groups.contains(g)) {
            groups.remove(g);
            g.removeLocalStudent(this);
        }
    }

    // equals et hashCode


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalStudentEntity that)) return false;
        return Objects.equals(emailUniv, that.emailUniv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailUniv);
    }
}
