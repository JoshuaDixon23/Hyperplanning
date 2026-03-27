package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
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

    // ------------------ MAPPERS ------------------

    private InternationalStudentEntity toDomain(InternationalStudent s){
        if (s == null) return null;
        return new InternationalStudentEntity(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv(),
                s.getEmailPersonal()
        );
    }

    private InternationalStudent toJpa(InternationalStudentEntity s) {
        if (s == null) return null;
        return new InternationalStudent(
                s.getFirstName(),
                s.getLastName(),
                s.getEmailUniv(),
                s.getEmailPersonal()
        );
    }

    // ------------------ FIND ------------------

    public List<InternationalStudentEntity> findAll(int pageNumber, int pageSize){
        return internationalStudentRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Optional<InternationalStudentEntity> findById(Long id){
        return internationalStudentRepository.findById(id)
                .map(this::toDomain);
    }

    // ------------------ CREATE ------------------

    @Transactional
    public InternationalStudentEntity create(String fName, String lName, String emailPersonal) {

        InternationalStudentEntity entity =
                InternationalStudentEntity.InternationalStudentFactory(fName, lName, emailPersonal);

        InternationalStudent jpa = toJpa(entity);

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