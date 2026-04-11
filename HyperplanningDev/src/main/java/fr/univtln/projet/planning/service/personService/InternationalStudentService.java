package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.mapper.person.InternationalStudentMapper;
import fr.univtln.projet.planning.modele.person.InternationalStudent;
import fr.univtln.projet.planning.repository.personRepository.InternationalStudentRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class InternationalStudentService {

    private final InternationalStudentRepository internationalStudentRepository;

    public InternationalStudentService(InternationalStudentRepository internationalStudentRepository) {
        this.internationalStudentRepository = internationalStudentRepository;
    }

    // ------------------ FIND ------------------

    public List<InternationalStudentEntity> findAll(int pageNumber, int pageSize){
        return internationalStudentRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(InternationalStudentMapper::toDomain)
                .toList();
    }

    /*
    public List<InternationalStudentEntity> findAll() {
        return internationalStudentRepository.findAll()
                .stream()
                .map(InternationalStudentMapper::toDomain)
                .toList();
    }

     */

    public Optional<InternationalStudentEntity> findById(Long id){
        return internationalStudentRepository.findById(id)
                .map(InternationalStudentMapper::toDomain);
    }

    // ------------------ CREATE ------------------

    @Transactional
    public InternationalStudentEntity create(String fName, String lName, String emailPersonal) {

        InternationalStudentEntity entity =
                InternationalStudentEntity.InternationalStudentFactory(fName, lName, emailPersonal);

        InternationalStudent jpa = InternationalStudentMapper.toJpa(entity);

        internationalStudentRepository.save(jpa);

        return entity;
    }

    // ------------------ GET ------------------

    public InternationalStudent getByEmailUniv(String emailUniv) {
        return internationalStudentRepository.findByEmailUniv(emailUniv);
    }

    // ------------------ DELETE ------------------

    @Transactional
    public void delete(String emailUniv) {
        InternationalStudent entity = getByEmailUniv(emailUniv);
        internationalStudentRepository.delete(entity);
    }
}