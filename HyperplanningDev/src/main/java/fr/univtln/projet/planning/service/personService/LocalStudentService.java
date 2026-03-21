package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
import fr.univtln.projet.planning.repository.personRepository.LocalStudentRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class LocalStudentService {
    private final LocalStudentRepository localStudentRepository;
    private final PromoRepository promoRepository;
    //private final EntityManager entityManager;

    // mapper from JPA to Domain (Entity)
    private LocalStudentEntity toDomain(LocalStudent s){
        if (s == null) return null;
        return new LocalStudentEntity(s.getFirstName(), s.getLastName(), s.getEmailUniv(), s.getEmailPersonal() /*,promoRepository.toDomainEntity(s.getPromo())*/);
    }

    // mapper from Domain (Entity) to JPA
    private LocalStudent toJpa(LocalStudentEntity s){
        if (s == null) return null;
        return new LocalStudent(s.getFirstName(), s.getLastName(), s.getEmailUniv(), s.getEmailPersonal() );
    }

    public LocalStudentService(LocalStudentRepository localStudentRepository, PromoRepository promoRepository) {
        this.localStudentRepository = localStudentRepository;
        this.promoRepository = promoRepository;
    }

    public List<LocalStudentEntity> findAll(int pageNumber, int pageSize){
        return localStudentRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Optional<LocalStudentEntity> findById(Long id){
        return localStudentRepository.findById(id)
                .map(this::toDomain);
    }

    @Transactional
    public LocalStudentEntity create(String fName, String lName, String emailPersonal) {
        // Step 0 : Create Entity (may be omitted by using another class CreateStudentRequest or by just passing entity class
        LocalStudentEntity entity = LocalStudentEntity.LocalStudentFactory(fName, lName, emailPersonal);

        // Step 1: Convert Entity (pure Java) to JPA via mapper
        LocalStudent jpa = toJpa(entity);

        // Step 2: Check persistence integrity
        // should be verified and managed in order to regenerate mail certain amount of times to find the unique one

        // Step 3: Persist and Return Entity pure Java
        localStudentRepository.save(jpa);
        return entity;
    }

    @Transactional
    public LocalStudentEntity createWithPromo(String fName, String lName, String emailPersonal, PromoEntity promo) {
        // Step 0 : Create Entity (may be omitted by using another class CreateStudentRequest or by just passing entity class
        LocalStudentEntity entity = LocalStudentEntity.LocalStudentFactory(fName, lName, emailPersonal);
        entity.setPromo(promo);

        // Step 1: Convert Entity (pure Java) to JPA via mapper
        LocalStudent jpa = toJpa(entity);

        // Step 2: Check persistence integrity
        // should be verified and managed in order to regenerate mail certain amount of times to find the unique one

        // Step 3: Persist and Return Entity pure Java
        localStudentRepository.save(jpa);
        return entity;
    }

    public LocalStudent getByEmailUniv(String emailUniv) {
        return localStudentRepository.findByEmailUniv(emailUniv);
    }


    @Transactional
    public void delete(String emailUniv) {
        LocalStudent entity = getByEmailUniv(emailUniv); // Reuse retrieval logic to handle 404
        localStudentRepository.delete(entity);
    }
}