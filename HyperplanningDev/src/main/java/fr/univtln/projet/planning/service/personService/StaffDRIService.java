package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.StaffDRIEntity;
import fr.univtln.projet.planning.mapper.person.StaffDRIMapper;
import fr.univtln.projet.planning.modele.person.StaffDRI;
import fr.univtln.projet.planning.repository.personRepository.StaffDRIRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class StaffDRIService {

    private final StaffDRIRepository staffDRIRepository;

    public StaffDRIService(StaffDRIRepository staffDRIRepository) {
        this.staffDRIRepository = staffDRIRepository;
    }

    // ------------------ FIND ------------------

    public List<StaffDRIEntity> findAll(int pageNumber, int pageSize){
        return staffDRIRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(StaffDRIMapper::toDomain)
                .toList();
    }

    public Optional<StaffDRIEntity> findById(Long id){
        return staffDRIRepository.findById(id)
                .map(StaffDRIMapper::toDomain);
    }

    // ------------------ CREATE ------------------

    @Transactional
    public StaffDRIEntity create(String fName, String lName) {

        StaffDRIEntity entity = StaffDRIEntity.StaffDRIFactory(fName, lName);

        StaffDRI jpa = StaffDRIMapper.toJpa(entity);

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