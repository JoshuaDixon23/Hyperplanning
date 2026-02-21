package fr.univtln.projet.planning.entity.person;

public class Student extends User{
    private String emailPersonal;

    private Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public static Student StudentFactory(String fname, String lname, String emailPersonal) {
        Student s =  UserFactory(fname, lname, Student::new);
        s.emailPersonal = emailPersonal.toLowerCase();
        return s;
    }

    public void setEmailPersonal(String emailPersonal) {
        this.emailPersonal = emailPersonal;
    }

    public String getEmailPersonal() {
        return emailPersonal;
    }
}
