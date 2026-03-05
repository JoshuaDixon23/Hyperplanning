package fr.univtln.projet.planning.modele.planning;

import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.infrastructure.Room;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Course")
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    
    private LocalDate date;
    private LocalTime startTime;
    private int duration; // Durée en minutes
    private String courseType; // CM, TD, TP, CC

    @ManyToOne
    @JoinColumn(name = "idRoom")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "moduleCode")
    private Module module;

    @ManyToMany
    @JoinTable(
        name = "Group_Course",
        joinColumns = @JoinColumn(name = "courseId"),
        inverseJoinColumns = @JoinColumn(name = "groupId")
    )
    private Set<Group> groups = new HashSet<>();
}