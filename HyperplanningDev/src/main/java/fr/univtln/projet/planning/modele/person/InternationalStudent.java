package fr.univtln.projet.planning.modele.person;

import fr.univtln.projet.planning.modele.international.BasketFinal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity
@Table(name = "InternationalStudent")
@Getter
@Setter
public class InternationalStudent extends Student {

    protected InternationalStudent() {
        super();
    }

    public InternationalStudent(String firstName, String lastName, String emailUniv, String emailPersonal) {
        super(firstName, lastName, emailUniv, emailPersonal);
    }

    @OneToOne
    @JoinColumn(name = "basket_id") //
    private BasketFinal basket;

    public BasketFinal getBasket() {
        return basket;
    }

    public void setBasket(BasketFinal basket) {
        this.basket = basket;
    }

    /*
    public static InternationalStudent InternationalStudentFactory(String fname, String lname, String emailPersonal) {
        
        InternationalStudent s = UserFactory(fname, lname, InternationalStudent::new);
        
        // Ajout spécifique à l'étudiant : l'email personnel formaté
        if (emailPersonal != null) {
            s.setEmailPersonal(emailPersonal.toLowerCase());
        }
        
        return s;
    }
     */


}


