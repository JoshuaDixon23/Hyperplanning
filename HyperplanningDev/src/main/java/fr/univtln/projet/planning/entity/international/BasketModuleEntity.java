package fr.univtln.projet.planning.entity.international;

import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.modele.planning.Module;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class BasketModuleEntity {

    @Setter
    private InternationalStudentEntity internationalStudentEntity;

    private Set<Module> modules;

    public BasketModuleEntity(InternationalStudentEntity student) {
        this.internationalStudentEntity = student;
        this.modules = new HashSet<>();
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    public void removeModule(Module module) {
        modules.remove(module);
    }
}