package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.AdminEntity;
import fr.univtln.projet.planning.modele.person.Admin;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class AdminService {

    private final AdminRepository adminRepository;

    // ------------------ MAPPERS ------------------

    public AdminEntity toDomain(Admin a){
        if (a == null) return null;
        return new AdminEntity(
                a.getFirstName(),
                a.getLastName(),
                a.getEmailUniv()
        );
    }

    public Admin toJpa(AdminEntity a){
        if (a == null) return null;
        return new Admin(
                a.getFirstName(),
                a.getLastName(),
                a.getEmailUniv()
        );
    }

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // ------------------ FIND ------------------

    public List<AdminEntity> findAll(int pageNumber, int pageSize){
        return adminRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Optional<AdminEntity> findById(Long id){
        return adminRepository.findById(id)
                .map(this::toDomain);
    }

    // ------------------ CREATE ------------------

    @Transactional
    public AdminEntity create(String fName, String lName) {
        AdminEntity entity = AdminEntity.AdminFactory(fName, lName);
        Admin jpa = toJpa(entity);
        Admin saved = adminRepository.save(jpa);
        return toDomain(saved);
    }

    // ------------------ GET ------------------

    public AdminEntity findByEmailUniv(String emailUniv) {
        return toDomain(adminRepository.findByEmailUniv(emailUniv));
    }

    // ------------------ DELETE ------------------

    @Transactional
    public void delete(String emailUniv) {
        AdminEntity entity = findByEmailUniv(emailUniv);
        Admin jpa = toJpa(entity);
        adminRepository.delete(jpa);
    }

    public Admin findJpaByEmailUniv(String adminEmailUniv) {
        return adminRepository.findByEmailUniv(adminEmailUniv);
    }
}