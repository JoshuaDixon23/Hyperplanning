package fr.univtln.projet.planning.entity.person;

import fr.univtln.projet.planning.entity.international.Basket;

public class InternationalStudent extends Student {
    private Basket basket;

    private InternationalStudent(String fname, String lname){
        super(fname, lname);
        this.basket = new Basket();
    }

    public static InternationalStudent InternationalStudentFactory(String fname, String lname, String emailPersonal) {
        InternationalStudent s =  UserFactory(fname, lname, InternationalStudent::new);
        s.setEmailPersonal(emailPersonal.toLowerCase());
        return s;
    }
}
