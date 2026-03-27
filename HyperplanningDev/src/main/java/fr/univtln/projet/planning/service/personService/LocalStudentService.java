package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.academic.PromoEntity;
import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.mapper.academic.GroupMapper;
import fr.univtln.projet.planning.mapper.academic.PromoMapper;
import fr.univtln.projet.planning.mapper.person.LocalStudentMapper;
import fr.univtln.projet.planning.modele.academic.Promo;
import fr.univtln.projet.planning.modele.academic.StudyLevel;
import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.repository.personRepository.LocalStudentRepository;
import fr.univtln.projet.planning.service.academicService.PromoService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class LocalStudentService {
    private final LocalStudentRepository localStudentRepository;
    private final PromoService promoService;
    //private final GroupService groupService;
    //private final EntityManager entityManager;

    public LocalStudentService(LocalStudentRepository localStudentRepository, PromoService promoService
                               /*, GroupService groupService*/) {
        this.localStudentRepository = localStudentRepository;
        this.promoService = promoService;
        //this.groupService = groupService;
    }

    public List<LocalStudentEntity> findAll(int pageNumber, int pageSize){
        return localStudentRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(LocalStudentMapper::toDomain)
                .toList();
    }

    public Optional<LocalStudentEntity> findById(Long id){
        return localStudentRepository.findById(id)
                .map(LocalStudentMapper::toDomain);
    }

    @Transactional
    public LocalStudentEntity create(String fName, String lName, String emailPersonal) {
        // Step 0 : Create Entity (may be omitted by using another class CreateStudentRequest or by just passing entity class
        LocalStudentEntity entity = LocalStudentEntity.LocalStudentFactory(fName, lName, emailPersonal);

        // Step 1: Convert Entity (pure Java) to JPA via mapper
        LocalStudent jpa = LocalStudentMapper.toJpa(entity);

        // Step 2: Check persistence integrity
        // should be verified and managed in order to regenerate mail certain amount of times to find the unique one

        // Step 3: Persist and Return Entity pure Java
        localStudentRepository.save(jpa);
        return entity;
    }

    @Transactional
    public LocalStudentEntity createWithPromo(String fName, String lName, String emailPersonal, String promoName,
                                              int promoYear, StudyLevel studyLevel) {
        // Step 0 : Create Entity (may be omitted by using another class CreateStudentRequest or by just passing entity class
        Promo promoJpa = promoService.findJpaByNameAndYearAndStudyLevel(promoName, promoYear, studyLevel);
        LocalStudentEntity entity = LocalStudentEntity.LocalStudentFactory(fName, lName, emailPersonal);
        entity.setPromo(PromoMapper.toDomain(promoJpa));

        // Step 1: Convert Entity (pure Java) to JPA via mapper
        LocalStudent jpa = new LocalStudent(
                entity.getFirstName(), entity.getLastName(),
                entity.getEmailUniv(), entity.getEmailPersonal(), promoJpa);

        // Step 2: Check persistence integrity
        // should be verified and managed in order to regenerate mail certain amount of times to find the unique one

        // Step 3: Persist and Return Entity pure Java
        LocalStudent saved = localStudentRepository.save(jpa);
        return LocalStudentMapper.toDomain(saved);
    }

    public LocalStudent getByEmailUniv(String emailUniv) {
        return localStudentRepository.findByEmailUniv(emailUniv);
    }


    @Transactional
    public void delete(String emailUniv) {
        LocalStudent entity = getByEmailUniv(emailUniv); // Reuse retrieval logic to handle 404
        localStudentRepository.delete(entity);
    }

    @Transactional
    public void addStudentToGroup(String emailUniv, GroupEntity group){
        LocalStudent student = localStudentRepository.findByEmailUniv(emailUniv);
        if(student == null) throw new IllegalArgumentException("Student not found");
        student.addGroup(GroupMapper.toJpa(group)); // côté propriétaire Many-to-Many
        localStudentRepository.save(student); // persiste la relation
    }

    @Transactional
    public LocalStudentEntity setPromo(LocalStudentEntity student, PromoEntity promo){
        LocalStudent jpa = LocalStudentMapper.toJpa(student);
        jpa.setPromo(PromoMapper.toJpa(promo));
        LocalStudent saved = localStudentRepository.save(jpa);
        return LocalStudentMapper.toDomain(saved);
    }
}