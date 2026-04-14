package fr.univtln.projet.planning.entity.person;

import java.util.List;

import fr.univtln.projet.planning.entity.international.BasketFinalEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;

public class InternationalStudentEntity extends StudentEntity {
    private BasketFinalEntity basketFinal;

    public InternationalStudentEntity(String fname, String lname){
        super(fname, lname);
    }

    public InternationalStudentEntity(String firstName, String lastName, String emailUniv, String emailPersonal) {
        super(firstName, lastName);
        this.emailUniv = emailUniv;
        this.emailPersonal = emailPersonal;
    }

    public static InternationalStudentEntity InternationalStudentFactory(String fname, String lname, String emailPersonal) {
        InternationalStudentEntity s =  UserFactory(fname, lname, InternationalStudentEntity::new);
        s.setEmailPersonal(emailPersonal.toLowerCase());
        s.setBasketFinal(new BasketFinalEntity());
        return s;
    }

    public BasketFinalEntity getBasketFinal() {
        return basketFinal;
    }

    public void setBasketFinal(BasketFinalEntity basketFinal) {
        this.basketFinal = basketFinal;
    }

    public void setBasketFinal(List<ModuleEntity> modules) {
        if (this.basketFinal == null) {
            this.basketFinal = new BasketFinalEntity();
        } else {
            this.basketFinal.getModuleGroup().clear(); 
        }
        
        for (ModuleEntity module : modules) {
            this.basketFinal.addModuleGroup(module, null);
        }
    }
}