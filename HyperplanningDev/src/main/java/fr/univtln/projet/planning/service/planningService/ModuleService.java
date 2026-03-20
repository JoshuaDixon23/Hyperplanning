package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.planning.Course;
import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.repository.planningRepository.ModuleRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository modulaRepository) {
        this.moduleRepository = modulaRepository;

    }


    ModuleEntity toDomain(Module module) {
        if (module == null) return null;

        ModuleEntity.Builder builder = ModuleEntity.builder()
                .code(module.getCode())
                .name(module.getName())
                .language(module.getLanguage())
                .ECTS(module.getEcts());
                //.responsible(module.getResponsible()); // il faut du responsible entity

        return builder.build();
    }




    public Module toJpa(ModuleEntity module) {
        if (module == null) return null;

        Module.Builder builder = Module.builder()
                .code(module.getCode())
                .name(module.getName())
                //.responsible(module.getresponsible()) même problème de plus haut
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

        moduleRepository.save(module);

        return entity;
    }

    @Transactional
    public void delete(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module introuvable"));

        moduleRepository.delete(module);
    }

    // ################### VALIDATION ##################


}



