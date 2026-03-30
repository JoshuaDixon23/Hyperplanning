package fr.univtln.projet.planning.mapper.academic;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.modele.academic.Group;

public class GroupMapper {

    private GroupMapper() {
        // Prevent instantiation of utility class
    }

    public static GroupEntity toDomain(Group g) {
        if (g == null) return null;
        GroupEntity entity = GroupEntity.GroupFactory(
                g.getNum(),
                g.getType()
        );
        entity.setPromo(PromoMapper.toDomain(g.getPromo()));
        // toDomain of planning to add ?
        return entity;
    }

    public static Group toJpa(GroupEntity g) {
        if (g == null) return null;
        Group jpa = Group.GroupFactory(
                g.getNum(),
                g.getType()
        );
        jpa.setPromo(PromoMapper.toJpa(g.getPromo()));
        // toJpa of planning to add ?
        return jpa;
    }
}
