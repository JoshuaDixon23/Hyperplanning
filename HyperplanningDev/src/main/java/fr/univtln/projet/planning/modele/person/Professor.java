package fr.univtln.projet.planning.modele.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Professor")
@Getter
@Setter
public class Professor extends User {
    // Possibilité d'ajouter des attributs spécifiques plus tard (ex: bureau, grade...)
}