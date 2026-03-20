package fr.univtln.projet.planning.entity.international;
import fr.univtln.projet.planning.entity.academic.GroupEntity;


import java.util.HashMap;
import java.util.Map;

public class BasketFinalEntity {

    private Map<Module, GroupEntity> moduleGroup;

    public void BasketFinal() {
        this.moduleGroup = new HashMap<>();
    }

    public BasketFinalEntity() {
    }

    public void addModule(Module module) {}

    public Map<Module, GroupEntity> getModuleGroup() {
        return moduleGroup;
    }

    public void addModuleGroup(Module module, GroupEntity groupEntity) {
        moduleGroup.put(module, groupEntity);
    }

    public GroupEntity getGroup(Module module) {
        return moduleGroup.get(module);
    }

    public void removeModule(Module module) {
        moduleGroup.remove(module);
    }
}


