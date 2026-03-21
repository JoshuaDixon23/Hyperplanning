package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.planning.CourseType;
import fr.univtln.projet.planning.modele.planning.Language;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ProfessorService professorService;

    public ModuleService(ModuleRepository modulaRepository, ProfessorService professorService) {
        this.moduleRepository = modulaRepository;
        this.professorService = professorService;
    }

    ModuleEntity toDomain(Module module) {
        if (module == null) return null;

        ModuleEntity.Builder builder = ModuleEntity.builder()
                .code(module.getCode())
                .name(module.getName())
                .language(module.getLanguage())
                .ECTS(module.getEcts())
                .responsible(professorService.toDomain(module.getResponsible()));

        return builder.build();
    }

    public Module toJpa(ModuleEntity module) {
        if (module == null) return null;

        Module.Builder builder = Module.builder()
                .code(module.getCode())
                .name(module.getName())
                .responsible(professorService.toJpa(module.getResponsible()))
                .language(module.getLanguage())
                .ects(module.getECTS());

        return builder.build();
    }


    // ################### CRUD ##################

    public Optional<ModuleEntity> findById(Long id) {
        return moduleRepository.findById(id)
                .map(this::toDomain);
    }

    public List<ModuleEntity> findAll() {
        return moduleRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Transactional
    public ModuleEntity create(ModuleEntity entity) {
        Module module = toJpa(entity);
        Module saved = moduleRepository.save(module);
        return toDomain(saved);
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
        return toDomain(saved);
    }

    @Transactional
    public void delete(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module introuvable"));

        moduleRepository.delete(module);
    }

}



