package fr.univtln.projet.planning.service.infrastructureService;

import fr.univtln.projet.planning.entity.infrastructure.CampusEntity;
import fr.univtln.projet.planning.mapper.infrastracture.CampusMapper;
import fr.univtln.projet.planning.modele.infrastructure.Campus;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class CampusService {

    private final CampusRepository campusRepository;

    // ------------------ CONSTRUCTEUR ------------------

    public CampusService(CampusRepository campusRepository) {
        this.campusRepository = campusRepository;
    }

    // ------------------ CREATE ------------------

    public CampusEntity create(CampusEntity campusEntity) {
        Campus jpa = CampusMapper.toJpa(campusEntity);
        Campus saved = campusRepository.save(jpa);
        return CampusMapper.toDomain(saved);
    }

    @Transactional
    public CampusEntity create(String city, String imageFileName) {
        CampusEntity campusEntity =  CampusEntity.CampusFactory(city, imageFileName);
        Campus jpa = CampusMapper.toJpa(campusEntity);
        Campus saved = campusRepository.save(jpa);
        return CampusMapper.toDomain(saved);
    }

    // ------------------ FIND ------------------

    public CampusEntity findById(Long id) {
        return campusRepository.findById(id)
                .map(CampusMapper::toDomain)
                .orElse(null);
    }

    public List<CampusEntity> findAll() {
        return campusRepository.findAll()
                .stream()
                .map(CampusMapper::toDomain)
                .toList();
    }

    public CampusEntity findByCity(String city) {
        return campusRepository.findByCity(city)
                .map(CampusMapper::toDomain)
                .orElse(null);
    }

    public Campus findJpaByCity(String city) {
        return campusRepository.findByCity(city)
                .orElse(null);
    }
}