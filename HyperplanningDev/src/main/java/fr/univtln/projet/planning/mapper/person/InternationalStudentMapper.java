package fr.univtln.projet.planning.mapper.person;

import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.modele.person.InternationalStudent;

public class InternationalStudentMapper {

    private InternationalStudentMapper() {
        // Prevent instantiation of utility class
    }

    // to be completed with Basket/ HashMap that represents Modules by Group
    public static InternationalStudentEntity toDomain(InternationalStudent s){
        if (s == null) return null;
        return new InternationalStudentEntity(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv(),
                s.getEmailPersonal()
        );
    }

    public static InternationalStudent toJpa(InternationalStudentEntity s) {
        if (s == null) return null;
        return new InternationalStudent(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv(),
                s.getEmailPersonal()
        );
    }
}
