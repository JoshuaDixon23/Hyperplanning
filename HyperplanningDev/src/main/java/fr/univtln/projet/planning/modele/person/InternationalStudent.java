package fr.univtln.projet.planning.modele.person;

//import fr.univtln.projet.planning.modele.international.Basket; // Assure-toi que Basket est bien dans modele
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "InternationalStudents")
@Getter
@Setter
public class InternationalStudent extends Student {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "basket_id")
    private BasketFinal basketFinal;

    protected InternationalStudent() {
        super();
    }

    public InternationalStudent(String firstName, String lastName, String emailUniv, String emailPersonal) {
        super(firstName, lastName, emailUniv, emailPersonal);
    }

    public BasketFinal getBasketFinal() {
        return basketFinal;
    }

    public void setBasketFinal(BasketFinal basketFinal) {
        this.basketFinal = basketFinal;
    }

    public Long getId() {
        return super.getId();
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