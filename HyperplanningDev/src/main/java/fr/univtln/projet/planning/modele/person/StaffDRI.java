package fr.univtln.projet.planning.modele.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "StaffDRIs")
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
