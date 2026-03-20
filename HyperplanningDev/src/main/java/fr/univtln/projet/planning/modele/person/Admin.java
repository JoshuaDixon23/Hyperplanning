package fr.univtln.projet.planning.modele.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Admin")
@Getter
@Setter
public class Admin extends User {

    protected Admin() {
        super();
    }

    public Admin(String firstName, String lastName, String emailUniv) {
        super(firstName, lastName, emailUniv);
    }

    /*
    public static Admin AdminFactory(String fname, String lname) {
        return UserFactory(fname, lname, Admin::new);
    }

     */
}