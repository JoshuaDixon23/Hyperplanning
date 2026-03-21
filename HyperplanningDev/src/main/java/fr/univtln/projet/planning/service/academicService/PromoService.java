package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
import fr.univtln.projet.planning.service.personService.LocalStudentService;

import java.util.List;

public class PromoService {

    /*
    private final PromoRepository promoRepository;
    private final UFRService ufrService;
    private final GroupService groupService;
    private final LocalStudentService localStudentService;

    public PromoService(PromoRepository promoRepository,
                        UFRService ufrService,
                        GroupService groupService,
                        LocalStudentService localStudentService) {
        this.promoRepository = promoRepository;
        this.ufrService = ufrService;
        this.groupService = groupService;
        this.localStudentService = localStudentService;
    }

    // ------------------ MAPPERS ------------------

    public PromoEntity toDomain(Promo p) {
        if (p == null) return null;

        PromoEntity entity = PromoEntity.PromoFactory(
                p.getName(),
                p.getStudyLevel(),
                ufrService.toDomain(p.getUfr())
        );

        if (p.getGroups() != null) {
            p.getGroups().forEach(g -> entity.addGroup(groupService.toDomain(g)));
        }

        if (p.getLocalStudents() != null) {
            p.getLocalStudents().forEach(s -> entity.addStudent(localStudentService.toDomain(s)));
        }

        return entity;
    }

    public Promo toJpa(PromoEntity p) {
        if (p == null) return null;

        Promo jpa = Promo.PromoFactory(
                p.getName(),
                0,
                p.getStudyLevel(),
                ufrService.toJpa(p.getUfr())
        );

        if (p.getGroups() != null) {
            p.getGroups().forEach(g -> jpa.addGroup(groupService.toJpa(g)));
        }

        if (p.getLocalStudent() != null) {
            p.getLocalStudent().forEach(s -> jpa.addStudent(localStudentService.toJpa(s)));
        }

        return jpa;
    }

    // ------------------ CREATE ------------------

    public PromoEntity create(PromoEntity entity) {
        Promo saved = promoRepository.save(toJpa(entity));
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

     */
}