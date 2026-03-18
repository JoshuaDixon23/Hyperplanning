package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.LocalStudentEntity;
import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.repository.personRepository.LocalStudentRepository;

public class LocalStudentService {
    private final LocalStudentRepository localStudentRepository;

    public LocalStudentService(LocalStudentRepository localStudentRepository) {
        this.localStudentRepository = localStudentRepository;
    }

    private LocalStudentEntity toDomain(LocalStudent jpaEntity) {
        if (jpaEntity == null) return null;
        LocalStudentEntity domainEntity = new LocalStudentEntity(jpaEntity.getFirstName(), jpaEntity.getLastName(),
                jpaEntity.getEmailUniv(), jpaEntity.getEmailPersonal());
        return domainEntity;
    }
}