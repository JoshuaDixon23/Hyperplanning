package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.entity.international.BasketFinalEntity;

public class InternationalStudentEntity extends StudentEntity {
    private BasketFinalEntity basketFinal;

    private InternationalStudentEntity(String fname, String lname){
        super(fname, lname);
        this.basketFinal = new BasketFinalEntity();
    }



    public static InternationalStudentEntity InternationalStudentFactory(String fname, String lname, String emailPersonal) {
        InternationalStudentEntity s =  UserFactory(fname, lname, InternationalStudentEntity::new);
        s.setEmailPersonal(emailPersonal.toLowerCase());
        return s;
    }
}
