package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
import fr.univtln.projet.planning.service.personService.LocalStudentService;

import java.util.List;

public class PromoService {

    private final PromoRepository promoRepository;
    private final UFRService ufrService;

    public PromoService(PromoRepository promoRepository,
                        UFRService ufrService) {
        this.promoRepository = promoRepository;
        this.ufrService = ufrService;
    }

    // ------------------ MAPPERS ------------------

    public PromoEntity toDomain(Promo p) {
        if (p == null) return null;
        return PromoEntity.PromoFactory(p.getName(), p.getYear(), p.getStudyLevel(), ufrService.toDomain(p.getUfr()));
    }

    public Promo toJpa(PromoEntity p) {
        if (p == null) return null;
        return Promo.PromoFactory(p.getName(), p.getYear(), p.getStudyLevel(), ufrService.toJpa(p.getUfr()));
    }

    // ------------------ CREATE ------------------

    public PromoEntity create(PromoEntity entity) {
        Promo saved = promoRepository.save(toJpa(entity));
        return toDomain(saved);
    }

    public PromoEntity create(String name, int year, StudyLevel studyLevel, String nameUfr) {
        UFR ufr = ufrService.findJpaByName(nameUfr);
        Promo promo = Promo.PromoFactory(name, year, studyLevel, ufr);
        Promo saved = promoRepository.save(promo);
        return toDomain(saved);
    }

    // ------------------ FIND ------------------

    public PromoEntity findById(Long id) {
        return promoRepository.findById(id)
                .map(this::toDomain)
                .orElse(null);
    }

    public List<PromoEntity> findAll() {
        return promoRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Promo findJpaByNameAndStudyLevelAndYear(String name, int year, StudyLevel studyLevel) {
        return promoRepository.findByNameAndStudyLevelAndYear(name, year, studyLevel);
    }
}