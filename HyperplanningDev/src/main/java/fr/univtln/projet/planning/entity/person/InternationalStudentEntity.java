package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.entity.international.BasketEntity;

public class InternationalStudentEntity extends StudentEntity {
    private BasketEntity basket;

    private InternationalStudentEntity(String fname, String lname){
        super(fname, lname);
        this.basket = new BasketEntity();
    }



    public static InternationalStudentEntity InternationalStudentFactory(String fname, String lname, String emailPersonal) {
        InternationalStudentEntity s =  UserFactory(fname, lname, InternationalStudentEntity::new);
        s.setEmailPersonal(emailPersonal.toLowerCase());
        return s;
    }
}
