package fr.univtln.projet.planning.entity.international;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;

import java.util.HashMap;
import java.util.Map;

public class BasketFinalEntity {

    private Map<ModuleEntity, GroupEntity> moduleGroup;

    public BasketFinalEntity() {
        this.moduleGroup = new HashMap<>();
    }

    public Map<ModuleEntity, GroupEntity> getModuleGroup() {
        return moduleGroup;
    }

    public void addModuleGroup(ModuleEntity module, GroupEntity groupEntity) {
        moduleGroup.put(module, groupEntity);
    }

    public GroupEntity getGroup(ModuleEntity module) {
        return moduleGroup.get(module);
    }

    public void removeModule(ModuleEntity module) {
        moduleGroup.remove(module);
    }
}