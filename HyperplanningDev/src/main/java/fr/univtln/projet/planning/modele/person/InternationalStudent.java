package fr.univtln.projet.planning.modele.person;

//import fr.univtln.projet.planning.modele.international.Basket; // Assure-toi que Basket est bien dans modele
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "InternationalStudent")
@Getter
@Setter
public class InternationalStudent extends Student {

    // @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "idBasket")
    // private Basket basket;

    protected InternationalStudent() {
        super();
        // this.basket = new Basket(); 
    }

    private InternationalStudent(String firstName, String lastName) {
        super(firstName, lastName);
        //this.basket = new Basket();
    }

    public static InternationalStudent InternationalStudentFactory(String fname, String lname, String emailPersonal) {
        
        InternationalStudent s = UserFactory(fname, lname, InternationalStudent::new);
        
        // Ajout spécifique à l'étudiant : l'email personnel formaté
        if (emailPersonal != null) {
            s.setEmailPersonal(emailPersonal.toLowerCase());
        }
        
        return s;
    }
}