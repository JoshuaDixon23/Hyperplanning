package fr.univtln.projet.planning.service.planningService;

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
        Module module = ModuleMapper.toJpa(entity);
        Module saved = moduleRepository.save(module);
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
                .toList();
    }

}



