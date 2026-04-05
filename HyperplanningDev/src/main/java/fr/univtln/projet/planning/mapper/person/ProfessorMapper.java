package fr.univtln.projet.planning.mapper.person;

import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import fr.univtln.projet.planning.modele.person.Professor;

public class ProfessorMapper {

    private ProfessorMapper() {
        // Prevent instantiation of utility class
    }

    public static ProfessorEntity toDomain(Professor professor) {
        if (professor == null) return null;

        return ProfessorEntity.ProfessorFactory(
                professor.getFirstName(),
                professor.getLastName(),
                professor.getEmailUniv()
        );
    }

    public static Professor toJpa(ProfessorEntity entity) {
        if (entity == null) return null;

        return new Professor(
                entity.getName(),
                entity.getSurname(),
                //"email@email@email"   //verifier la méthode
                entity.getEmailUniv()
        );
    }
}
