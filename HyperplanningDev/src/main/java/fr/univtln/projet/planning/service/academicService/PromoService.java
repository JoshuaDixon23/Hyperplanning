package fr.univtln.projet.planning.service.academicService;

import java.util.List;
import java.util.stream.Collectors;

import fr.univtln.projet.planning.entity.academic.PromoEntity;             // Modèle JPA (BDD)
import fr.univtln.projet.planning.modele.academic.Promo;       // Modèle Métier (DTO)
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
import fr.univtln.projet.planning.service.personService.LocalStudentService;

public class PromoService {
    /*

    private final PromoRepository promoRepository;
    private final UFRService ufrService;
    private final GroupService groupService;
    private final LocalStudentService localStudentService;

    public PromoService(PromoRepository promoRepository, UFRService ufrService, GroupService groupService, LocalStudentService localStudentService) {
        this.promoRepository = promoRepository;
        this.ufrService = ufrService;
        this.groupService = groupService;
        this.localStudentService = localStudentService;
    }

    // --- MÉTHODES MÉTIER ---

    public PromoEntity createPromo(PromoEntity domainPromo) {
        // 1. Convertir l'objet métier en modèle JPA
        Promo jpaPromo = toJpaModel(domainPromo);

        // 2. Sauvegarder via le repository
        Promo savedPromo = promoRepository.save(jpaPromo);

        // 3. Reconvertir le modèle sauvegardé en objet métier et le retourner
        return toDomainEntity(savedPromo);
    }

    public PromoEntity getPromoById(Long id) {
        return promoRepository.findById(id)
                .map(this::toDomainEntity)
                .orElse(null);
    }

    public List<PromoEntity> getAllPromos() {
        return promoRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    // --- MAPPINGS ---
    private PromoEntity toDomainEntity(Promo jpaPromo) {
        if (jpaPromo == null) return null;

        PromoEntity domainPromo = PromoEntity.PromoFactory(
            jpaPromo.getName(), 
            jpaPromo.getStudyLevel(), 
            ufrService.toDomainEntity(jpaPromo.getUfr())
        );

        if (jpaPromo.getGroups() != null) {
            jpaPromo.getGroups().forEach(g -> domainPromo.addGroup(groupService.toDomainEntity(g)));
        }
        if (jpaPromo.getLocalStudents() != null) {
            jpaPromo.getLocalStudents().forEach(s -> domainPromo.addStudent(localStudentService.toDomainEntity(s)));
        }
        return domainPromo;
    }

    private Promo toJpaModel(PromoEntity domainPromo) {
        if (domainPromo == null) return null;

        Promo jpaPromo = Promo.PromoFactory(
            domainPromo.getName(), 
            0, // year temporaire
            domainPromo.getStudyLevel(), 
            ufrService.toJpaModel(domainPromo.getUfr())
        );

        if (domainPromo.getGroups() != null) {
            domainPromo.getGroups().forEach(g -> jpaPromo.addGroup(groupService.toJpaModel(g)));
        }
        if (domainPromo.getLocalStudent() != null) {
            domainPromo.getLocalStudent().forEach(s -> jpaPromo.addStudent(localStudentService.toJpaModel(s)));
        }
        return jpaPromo;
    }

     */
}