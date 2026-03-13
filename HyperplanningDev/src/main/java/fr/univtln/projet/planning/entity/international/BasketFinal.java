package fr.univtln.projet.planning.entity.international;
import fr.univtln.projet.planning.entity.academic.Group;


import java.util.HashMap;
import java.util.Map;

public class BasketFinal {

    private final Map<Module, Group> moduleGroup;

    public BasketFinal() {
        this.moduleGroup = new HashMap<>();
    }
    public void addModule(Module module) {}

    public Map<Module, Group> getModuleGroup() {
        return moduleGroup;
    }

    public void addModuleGroup(Module module, Group group) {
        moduleGroup.put(module, group);
    }

    public Group getGroup(Module module) {
        return moduleGroup.get(module);
    }

    public void removeModule(Module module) {
        moduleGroup.remove(module);
    }
}