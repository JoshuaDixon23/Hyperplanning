package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.AdminEntity;
import fr.univtln.projet.planning.mapper.person.AdminMapper;
import fr.univtln.projet.planning.modele.person.Admin;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // ------------------ FIND ------------------

    public List<AdminEntity> findAll(int pageNumber, int pageSize){
        return adminRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(AdminMapper::toDomain)
                .toList();
    }

    public Optional<AdminEntity> findById(Long id){
        return adminRepository.findById(id)
                .map(AdminMapper::toDomain);
    }

    // ------------------ CREATE ------------------

    @Transactional
    public AdminEntity create(String fName, String lName) {
        AdminEntity entity = AdminEntity.AdminFactory(fName, lName);
        Admin jpa = AdminMapper.toJpa(entity);
        Admin saved = adminRepository.save(jpa);
        return AdminMapper.toDomain(saved);
    }

    // ------------------ GET ------------------

    public AdminEntity findByEmailUniv(String emailUniv) {
        return AdminMapper.toDomain(adminRepository.findByEmailUniv(emailUniv));
    }

    // ------------------ DELETE ------------------

    @Transactional
    public void delete(String emailUniv) {
        AdminEntity entity = findByEmailUniv(emailUniv);
        Admin jpa = AdminMapper.toJpa(entity);
        adminRepository.delete(jpa);
    }

    public Admin findJpaByEmailUniv(String adminEmailUniv) {
        return adminRepository.findByEmailUniv(adminEmailUniv);
    }
}