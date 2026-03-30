package fr.univtln.projet.planning.mapper.academic;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.modele.academic.Promo;

public class PromoMapper {

    private PromoMapper() {
        // Prevent instantiation of utility class
    }

    public static PromoEntity toDomain(Promo p) {
        if (p == null) return null;
        return PromoEntity.PromoFactory(p.getName(), p.getYear(), p.getStudyLevel(), UFRMapper.toDomain(p.getUfr()));
    }

    public static Promo toJpa(PromoEntity p) {
        if (p == null) return null;
        return Promo.PromoFactory(p.getName(), p.getYear(), p.getStudyLevel(), UFRMapper.toJpa(p.getUfr()));
    }
}
