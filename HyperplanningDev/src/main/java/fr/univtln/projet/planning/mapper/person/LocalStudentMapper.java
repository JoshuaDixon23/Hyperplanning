package fr.univtln.projet.planning.mapper.person;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.mapper.academic.GroupMapper;
import fr.univtln.projet.planning.mapper.academic.PromoMapper;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.person.LocalStudent;

public class LocalStudentMapper {

    private LocalStudentMapper() {
        // Prevent instantiation of utility class
    }

    public static LocalStudentEntity toDomain(LocalStudent s){
        if (s == null) return null;
        LocalStudentEntity entity = new LocalStudentEntity(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv(),
                s.getEmailPersonal(),
                PromoMapper.toDomain(s.getPromo())
        );
        // synchroniser les groupes
        if (s.getGroups() != null){
            for(Group g : s.getGroups()){
                entity.addGroup(GroupMapper.toDomain(g));
            }
        }
        return entity;
    }

    public static LocalStudent toJpa(LocalStudentEntity s){
        if (s == null) return null;
        LocalStudent jpa = new LocalStudent(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv(),
                s.getEmailPersonal(),
                PromoMapper.toJpa(s.getPromo())
        );
        // synchroniser les groupes
        if (s.getGroups() != null){
            for(GroupEntity g : s.getGroups()){
                jpa.addGroup(GroupMapper.toJpa(g));
            }
        }
        return jpa;
    }
}
