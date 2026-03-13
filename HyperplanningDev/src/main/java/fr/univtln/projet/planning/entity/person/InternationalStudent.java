package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.entity.international.BasketFinal;

public class InternationalStudent extends Student {
    private BasketFinal basketfinal;

    private InternationalStudent(String fname, String lname){
        super(fname, lname);
        this.basketfinal = new BasketFinal();
    }

    public static InternationalStudent InternationalStudentFactory(String fname, String lname, String emailPersonal) {
        InternationalStudent s =  UserFactory(fname, lname, InternationalStudent::new);
        s.setEmailPersonal(emailPersonal.toLowerCase());
        return s;
    }
}
