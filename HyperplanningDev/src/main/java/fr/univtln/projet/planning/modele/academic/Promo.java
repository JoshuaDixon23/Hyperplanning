package fr.univtln.projet.planning.modele.academic;

import fr.univtln.projet.planning.modele.academic.UFR;

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
@Table(name = "Promo")
@Getter
@Setter
public class Promo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promoId;
    
    private String name;
    private int year;
    
    @ManyToOne
    @JoinColumn(name = "idUFR")
    private UFR ufr;
}