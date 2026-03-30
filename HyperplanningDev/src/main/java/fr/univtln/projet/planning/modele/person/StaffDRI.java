package fr.univtln.projet.planning.modele.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "StaffDRI")
@Getter
@Setter
public class StaffDRI extends User{
    protected StaffDRI() {
        super();
    }

    public StaffDRI(String firstName, String lastName, String emailUniv) {
        super(firstName, lastName, emailUniv);
    }
}
