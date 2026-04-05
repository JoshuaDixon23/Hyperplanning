package fr.univtln.projet.planning.service.planningService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.mapper.planning.ModuleMapper;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.planning.CourseType;
import fr.univtln.projet.planning.modele.planning.Language;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import jakarta.transaction.Transactional;


public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ProfessorService professorService;

    public ModuleService(ModuleRepository modulaRepository, ProfessorService professorService) {
        this.moduleRepository = modulaRepository;
        this.professorService = professorService;
    }

    @Transactional
    public ModuleEntity create(ModuleEntity entity) {
        Professor responsibleJpa = null;
        if (entity.getResponsible() != null) {
            responsibleJpa = professorService.findJpaByEmailUniv(entity.getResponsible().getEmailUniv());
            
            if (responsibleJpa == null) {
                throw new IllegalArgumentException("Professeur responsable introuvable en base avec l'email : " + entity.getResponsible().getEmailUniv());
            }
        }

        Module moduleJpa = Module.builder()
                .code(entity.getCode())
                .name(entity.getName())
                .language(entity.getLanguage())
                .ects(entity.getECTS())
                .responsible(responsibleJpa) 
                .build();

        Module saved = moduleRepository.save(moduleJpa);

        return ModuleMapper.toDomain(saved);
    }


    @Transactional
    public ModuleEntity create(String code, String name, Language language, float ects, String responsibleEmailUniv,
                               Map<CourseType, Float> courseHours) {
        Professor responsible = professorService.findJpaByEmailUniv(responsibleEmailUniv);
        Module jpa = Module.builder()
                .code(code)
                .name(name)
                .language(language)
                .ects(ects)
                .responsible(responsible)
                .build();
        Module saved = moduleRepository.save(jpa);
        return ModuleMapper.toDomain(saved);
    }

    @Transactional
    public ModuleEntity update(ModuleEntity entity) {
        Module existingModule = findJpaByCode(entity.getCode());
        
        if (existingModule == null) {
            throw new IllegalArgumentException("Module introuvable en base : " + entity.getCode());
        }

        Professor responsibleJpa = null;
        if (entity.getResponsible() != null) {
            responsibleJpa = professorService.findJpaByEmailUniv(entity.getResponsible().getEmailUniv());
            
            if (responsibleJpa == null) {
                throw new IllegalArgumentException("Professeur responsable introuvable en base avec l'email : " + entity.getResponsible().getEmailUniv());
            }
        }

        existingModule.setName(entity.getName());
        existingModule.setLanguage(entity.getLanguage());
        existingModule.setEcts(entity.getECTS());
        existingModule.setResponsible(responsibleJpa);

        Module saved = moduleRepository.save(existingModule);

        return ModuleMapper.toDomain(saved);
    }

    @Transactional
    public void delete(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module introuvable"));

        moduleRepository.delete(module);
    }


    public void delete(ModuleEntity module) {
        Module jpaModule = findJpaByCode(module.getCode());
        moduleRepository.delete(jpaModule);
    }

    public ModuleEntity findByCode(String code) {
        return ModuleMapper.toDomain(moduleRepository.findByCode(code));
    }

    public Module findJpaByCode(String code) {
        return moduleRepository.findByCode(code);
    }

    public Optional<ModuleEntity> findById(Long id) {
        return moduleRepository.findById(id)
                .map(ModuleMapper::toDomain);
    }

    public List<ModuleEntity> findAll() {
            return moduleRepository.findAll()
                    .stream()
                    .map(ModuleMapper::toDomain)
                    .sorted(Comparator.comparing(ModuleEntity::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        }

}



