package fr.univtln.projet.planning.entity.international;
import fr.univtln.projet.planning.entity.person.InternationalStudent;

import java.util.HashSet;
import java.util.Set;

public class BasketModule {

    private InternationalStudent internationalStudent;
    private final Set<Module> modules;

    public BasketModule(InternationalStudent internationalStudent) {
        this.internationalStudent = internationalStudent;
        this.modules = new HashSet<>();
    }

    public InternationalStudent getInternationalStudent() {
        return internationalStudent;
    }


    public Set<Module> getModules() {
        return modules;
    }

    public void setInternationalStudent(InternationalStudent internationalStudent) {
        this.internationalStudent = internationalStudent;
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    public void removeModule(Module module) {
        modules.remove(module);
    }
}