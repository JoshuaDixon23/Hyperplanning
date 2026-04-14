package fr.univtln.projet.planning.entity.international;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;

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

    public List<ModuleEntity> getListModules() {
        return new ArrayList<>(moduleGroup.keySet());
    }

    public void removeModule(ModuleEntity module) {
        moduleGroup.remove(module);
    }
}