package fr.univtln.projet.planning.entity.person;

public abstract class StudentEntity extends UserEntity {
    private String emailPersonal;

    protected StudentEntity(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public StudentEntity(String firstName, String lastName, String emailUniv, String emailPersonal) {
        super(firstName, lastName, emailUniv);
        this.emailPersonal = emailPersonal;
    }

    public void setEmailPersonal(String emailPersonal) {
        this.emailPersonal = emailPersonal;
    }

    public String getEmailPersonal() {
        return emailPersonal;
    }
}
