package fr.univtln.projet.planning.mapper.planning;

import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.mapper.person.ProfessorMapper;
import fr.univtln.projet.planning.modele.planning.Module;

public class ModuleMapper {

    private ModuleMapper() {
        // Prevent instantiation of utility class
    }

    public static ModuleEntity toDomain(fr.univtln.projet.planning.modele.planning.Module module) {
        if (module == null) return null;

        ModuleEntity.Builder builder = ModuleEntity.builder()
                .code(module.getCode())
                .name(module.getName())
                .language(module.getLanguage())
                .ECTS(module.getEcts())
                .responsible(ProfessorMapper.toDomain(module.getResponsible()));

        return builder.build();
    }

    public static Module toJpa(ModuleEntity module) {
        if (module == null) return null;

        fr.univtln.projet.planning.modele.planning.Module.Builder builder = Module.builder()
                .code(module.getCode())
                .name(module.getName())
                .responsible(ProfessorMapper.toJpa(module.getResponsible()))
                .language(module.getLanguage())
                .ects(module.getECTS());

        return builder.build();
    }
}
