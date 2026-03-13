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

    private Admin(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public static Admin AdminFactory(String fname, String lname) {
        return UserFactory(fname, lname, Admin::new);
    }
}