package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;

import fr.univtln.projet.planning.modele.academic.Promo;             // Modèle JPA (BDD)
import fr.univtln.projet.planning.entity.academic.PromoEntity;       // Modèle Métier (DTO)
// On aura aussi besoin des modèles UFR plus tard pour le mapping :
// import fr.univtln.projet.planning.modele.academic.UFR;
// import fr.univtln.projet.planning.entity.academic.UFREntity;

import java.util.List;
import java.util.stream.Collectors;

public class PromoService {

    private final PromoRepository promoRepository;

    public PromoService(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
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

    // --- MÉTHODES DE MAPPING (Traduction) ---

    /**
     * Convertit le modèle JPA (Base de données) vers l'objet Métier (PromoEntity)
     */
    private PromoEntity toDomainEntity(Promo jpaPromo) {
        if (jpaPromo == null) return null;

        // TODO: Mapper l'UFR JPA vers l'UFREntity métier
        // UFREntity ufrEntity = ufrMapper.toDomainEntity(jpaPromo.getUfr());
        
        PromoEntity domainPromo = PromoEntity.PromoFactory(
            jpaPromo.getName(), 
            jpaPromo.getStudyLevel(), 
            null // ufrEntity (Remplacer null quand l'UFR sera géré)
        );
        


        return domainPromo;
    }

    /**
     * Convertit l'objet Métier (PromoEntity) vers le modèle JPA (Base de données)
     */
    private Promo toJpaModel(PromoEntity domainPromo) {
        if (domainPromo == null) return null;

        // TODO: Mapper l'UFREntity métier vers l'UFR JPA
        // UFR jpaUfr = ufrMapper.toJpaModel(domainPromo.getUfr());

        // Attention : il manque 'year' dans PromoEntity. On met 0 en attendant.
        int anneeTemporaire = 0;

        Promo jpaPromo = Promo.PromoFactory(
            domainPromo.getName(), 
            anneeTemporaire, // TODO: à lier avec un futur domainPromo.getYear()
            domainPromo.getStudyLevel(), 
            null // jpaUfr (Remplacer null quand l'UFR sera géré)
        );

        // TODO: Boucler sur domainPromo.getGroups() pour les ajouter à jpaPromo
        // TODO: Boucler sur domainPromo.getLocalStudent() pour les ajouter à jpaPromo

        return jpaPromo;
    }
}