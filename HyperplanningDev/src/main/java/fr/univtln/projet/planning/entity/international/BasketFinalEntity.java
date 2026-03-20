package fr.univtln.projet.planning.entity.international;
import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import org.hibernate.mapping.List;


import java.util.HashMap;
import java.util.Map;

public class BasketFinalEntity {

    private Map<ModuleEntity, GroupEntity> moduleGroup;

    private Map<GroupEntity,ModuleEntity> planning;

    public void BasketFinal() {
        this.moduleGroup = new HashMap<>();
    }

    public BasketFinalEntity() {
    }

    public void addModule(Module module) {}

    public Map<ModuleEntity, GroupEntity> getModuleGroup() {
        return moduleGroup;
    }

    public void addModuleGroup(ModuleEntity module, GroupEntity groupEntity) {
        moduleGroup.put(module, groupEntity);
    }

    public GroupEntity getGroup(ModuleEntity module) {
        return moduleGroup.get(module);
    }

    public void removeModule(Module module) {
        moduleGroup.remove(module);
    }

    @Override
    public String toString() {
        return "BasketFinalEntity{" +
                "moduleGroup=" + moduleGroup +
                '}';
    }


}


