package fr.univtln.projet.planning.entity.person;

public abstract class Student extends User{
    private String emailPersonal;

    protected Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public void setEmailPersonal(String emailPersonal) {
        this.emailPersonal = emailPersonal;
    }

    public String getEmailPersonal() {
        return emailPersonal;
    }
}
