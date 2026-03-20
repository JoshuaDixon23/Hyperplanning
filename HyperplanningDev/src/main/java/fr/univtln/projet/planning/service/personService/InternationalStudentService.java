package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.repository.personRepository.InternationalStudentRepository;

public class InternationalStudentService{
    private final InternationalStudentRepository internationalStudentService;

    public InternationalStudentService(InternationalStudentRepository internationalStudentService) {
        this.internationalStudentService = internationalStudentService;
    }


}