package fr.univtln.projet.planning.modele.academic;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "UFR")
@Getter
@Setter
public class UFR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUFR;
    
    private String name;
    private String campus;
}