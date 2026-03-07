package fr.univtln.projet.planning.modele.academic;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PlanningGroup") // Evite le conflit avec le mot réservé SQL "Group"
@Getter
@Setter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    
    private String type; // TD, TP, CM

    @ManyToOne
    @JoinColumn(name = "promoId")
    private Promo promo;
}