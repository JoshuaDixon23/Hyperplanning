package fr.univtln.projet.planning.service.personService;

import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import fr.univtln.projet.planning.mapper.person.ProfessorMapper;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.repository.personRepository.ProfessorRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    // ################### CRUD ##################

    public Optional<ProfessorEntity> findById(Long id) {
        return professorRepository.findById(id)
                .map(ProfessorMapper::toDomain);
    }

    public List<ProfessorEntity> findAll() {
        return professorRepository.findAll()
                .stream()
                .map(ProfessorMapper::toDomain)
                .toList();
    }

    @Transactional
    public ProfessorEntity create(ProfessorEntity entity) {
        Professor professor = ProfessorMapper.toJpa(entity);
        professorRepository.save(professor);
        return entity;
    }

    @Transactional
    public void delete(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professor introuvable"));

        professorRepository.delete(professor);
    }

    public Professor findJpaByEmailUniv(String emailUniv) {
        return  professorRepository.findByEmailUniv(emailUniv);
    }
}