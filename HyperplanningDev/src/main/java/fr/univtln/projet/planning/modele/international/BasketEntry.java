package fr.univtln.projet.planning.modele.international;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.planning.Module;

import jakarta.persistence.*;

@Entity
public class BasketEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BasketFinal basket;

    @ManyToOne
    private Module module;

    @ManyToOne
    private Group group;

    protected BasketEntry() {
    }

    public BasketEntry(BasketFinal basket, Module module, Group group) {
        this.basket = basket;
        this.module = module;
        this.group = group;
    }

    public BasketEntry(BasketFinal basket, Module module, GroupEntity group) {
    }

    public Module getModule() {
        return module;
    }

    public Group getGroup() {
        return group;
    }
}