package fr.univtln.projet.planning.modele.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Students")
@Getter
@Setter
public class Student extends User {
    @Column(unique = true, nullable = false)
    private String emailPersonal;

    protected Student() {
        super();
    }

    /**
     * Protected constructor used by subclasses and factories.
     * @param firstName The student's first name
     * @param lastName  The student's last name
     */
    protected Student(String firstName, String lastName, String emailUniv, String emailPersonal) {
        super(firstName, lastName, emailUniv);
        this.emailPersonal = emailPersonal;
    }

    /**
     * Factory method to create a Student properly.
     * Passes the inputs to the parent UserFactory for formatting and email generation.
     * * @param fname The student's raw first name
     * @param lname The student's raw last name
     * @return A formatted Student instance
     */
    /*
    public static Student StudentFactory(String fname, String lname) {
        return UserFactory(fname, lname, Student::new);
    }
     */
}