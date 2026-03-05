package fr.univtln.projet.planning.modele.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Admin")
@Getter
@Setter
public class Admin extends User {
    // Les attributs hérités de User suffisent pour le moment
}