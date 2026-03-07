package fr.univtln.projet.planning.modele.planning;

import fr.univtln.projet.planning.modele.person.Professor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Module")
@Getter
@Setter
public class Module {
    @Id
    private String code; // Ex: M1101 (Pas de auto-increment ici car c'est un code texte)
    
    private String name;
    private String language;
    private int ects;

    @ManyToOne
    @JoinColumn(name = "responsibleId")
    private Professor responsible;
}