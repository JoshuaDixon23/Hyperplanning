package fr.univtln.projet.planning.entity.person;

import lombok.Getter;

@Getter
public abstract class StudentEntity extends UserEntity {
    protected String emailPersonal;

    // no email in constructor for consistent use of Factory for child classes
    protected StudentEntity(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public void setEmailPersonal(String emailPersonal) {
        this.emailPersonal = emailPersonal;
    }

}
