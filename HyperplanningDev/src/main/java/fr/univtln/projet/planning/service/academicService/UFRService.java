package fr.univtln.projet.planning.service.academicService;

import fr.univtln.projet.planning.entity.academic.UFREntity;
import fr.univtln.projet.planning.mapper.academic.UFRMapper;
import fr.univtln.projet.planning.modele.academic.UFR;
import fr.univtln.projet.planning.modele.infrastructure.Campus;
import fr.univtln.projet.planning.modele.person.Admin;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.service.infrastructureService.CampusService;
import fr.univtln.projet.planning.service.personService.AdminService;

import java.util.List;
public class UFRService {

    private final UFRRepository ufrRepository;
    private final AdminService adminService;
    private final CampusService campusService;

    public UFRService(UFRRepository ufrRepository,
                      AdminService adminService,
                      CampusService campusService) {
        this.ufrRepository = ufrRepository;
        this.adminService = adminService;
        this.campusService = campusService;
    }

    // ------------------ CREATE ------------------

    public UFREntity create(UFREntity entity) {
        UFR saved = ufrRepository.save(UFRMapper.toJpa(entity));
        return UFRMapper.toDomain(saved);
    }

    public UFREntity create(String name, String campusCity, String adminEmailUniv) {
        Campus campus = campusService.findJpaByCity(campusCity);
        if (campus == null) {
            throw new IllegalArgumentException("Campus not found: " + campusCity);
        }

        Admin admin = adminService.findJpaByEmailUniv(adminEmailUniv);
        if (admin == null) {
            throw new IllegalArgumentException("Admin not found: " + adminEmailUniv);
        }

        // 🔹 Créer directement l'objet JPA avec des objets déjà persistés
        UFR ufr = UFR.UFRFactory(name, campus, admin);

        UFR saved = ufrRepository.save(ufr);

        return UFRMapper.toDomain(saved);
    }

    // ------------------ FIND ------------------

    public UFREntity findById(Long id) {
        return ufrRepository.findById(id)
                .map(UFRMapper::toDomain)
                .orElse(null);
    }

    public UFREntity findByName(String name) {
        return ufrRepository.findByName(name)
                .map(UFRMapper::toDomain)
                .orElse(null);
    }

    public List<UFREntity> findAll() {
        return ufrRepository.findAll()
                .stream()
                .map(UFRMapper::toDomain)
                .toList();
    }

    public UFR findJpaByName(String name) {
        return ufrRepository.findByName(name)
                .orElse(null);
    }
}