package fr.univtln.projet.planning.modele.international;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class BasketFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Map Module -> Groupe
    @ManyToMany
    @MapKeyJoinColumn(name = "module_id")
    @JoinTable(
            name = "basket_module_group",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Map<ModuleEntity, GroupEntity> moduleGroup = new HashMap<>();

    // Map Groupe -> Module
    @ManyToMany
    @MapKeyJoinColumn(name = "group_id")
    @JoinTable(
            name = "basket_planning",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Map<GroupEntity, ModuleEntity> planning = new HashMap<>();

    // ✅ constructeur obligatoire JPA
    public BasketFinal() {
    }

    // ====== LOGIQUE MÉTIER ======

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

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "BasketFinal{" +
                "id=" + id +
                ", moduleGroup=" + moduleGroup +
                '}';
    }
}