package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.mapper.academic.PromoMapper;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;

import java.util.List;

public class PromoService {

    private final PromoRepository promoRepository;
    private final UFRService ufrService;

    public PromoService(PromoRepository promoRepository,
                        UFRService ufrService) {
        this.promoRepository = promoRepository;
        this.ufrService = ufrService;
    }

    // ------------------ CREATE ------------------

    public PromoEntity create(PromoEntity entity) {
        Promo saved = promoRepository.save(PromoMapper.toJpa(entity));
        return PromoMapper.toDomain(saved);
    }

    public PromoEntity create(String name, int year, StudyLevel studyLevel, String nameUfr) {
        UFR ufr = ufrService.findJpaByName(nameUfr);
        Promo promo = Promo.PromoFactory(name, year, studyLevel, ufr);
        Promo saved = promoRepository.save(promo);
        return PromoMapper.toDomain(saved);
    }

    // ------------------ FIND ------------------

    public PromoEntity findById(Long id) {
        return promoRepository.findById(id)
                .map(PromoMapper::toDomain)
                .orElse(null);
    }

    public List<PromoEntity> findAll() {
        return promoRepository.findAll()
                .stream()
                .map(PromoMapper::toDomain)
                .toList();
    }

    public Promo findJpaByNameAndYearAndStudyLevel(String name, int year, StudyLevel studyLevel) {
        return promoRepository.findByNameAndYearAndStudyLevel(name, year, studyLevel);
    }
}