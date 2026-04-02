package fr.univtln.projet.planning.entity.international;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.international.BasketFinal;
import fr.univtln.projet.planning.modele.planning.Module;
import jakarta.persistence.*;

@Entity
public class BasketEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BasketFinal basket;

    @ManyToOne
    private Module module;

    @ManyToOne
    private Group group;

    protected BasketEntryEntity() {
        // Constructeur vide requis par JPA
    }

    public BasketEntryEntity(BasketFinal basket, Module module, Group group) {
        this.basket = basket;
        this.module = module;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public BasketFinal getBasket() {
        return basket;
    }

    public Module getModule() {
        return module;
    }

    public Group getGroup() {
        return group;
    }
}