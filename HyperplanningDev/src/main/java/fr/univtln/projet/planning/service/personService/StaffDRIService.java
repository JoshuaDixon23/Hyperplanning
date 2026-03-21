package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.StaffDRIEntity;
import fr.univtln.projet.planning.modele.person.StaffDRI;
import fr.univtln.projet.planning.repository.personRepository.StaffDRIRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class StaffDRIService {

    private final StaffDRIRepository staffDRIRepository;

    // ------------------ MAPPERS ------------------

    private StaffDRIEntity toDomain(StaffDRI s){
        if (s == null) return null;
        return new StaffDRIEntity(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv()
        );
    }

    private StaffDRI toJpa(StaffDRIEntity s){
        if (s == null) return null;
        return new StaffDRI(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv()
        );
    }

    public StaffDRIService(StaffDRIRepository staffDRIRepository) {
        this.staffDRIRepository = staffDRIRepository;
    }

    // ------------------ FIND ------------------

    public List<StaffDRIEntity> findAll(int pageNumber, int pageSize){
        return staffDRIRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Optional<StaffDRIEntity> findById(Long id){
        return staffDRIRepository.findById(id)
                .map(this::toDomain);
    }

    // ------------------ CREATE ------------------

    @Transactional
    public StaffDRIEntity create(String fName, String lName) {

        StaffDRIEntity entity = StaffDRIEntity.StaffDRIFactory(fName, lName);

        StaffDRI jpa = toJpa(entity);

        staffDRIRepository.save(jpa);

        return entity;
    }

    // ------------------ GET ------------------

    public StaffDRI getByEmailUniv(String emailUniv) {
        return staffDRIRepository.findByEmailUniv(emailUniv);
    }

    // ------------------ DELETE ------------------

    @Transactional
    public void delete(String emailUniv) {
        StaffDRI entity = getByEmailUniv(emailUniv);
        staffDRIRepository.delete(entity);
    }
}