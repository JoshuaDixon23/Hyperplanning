package fr.univtln.projet.planning.modele.international;

import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.planning.Module;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class BasketFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation principale
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketEntry> entries = new ArrayList<>();

    // constructeur JPA obligatoire
    public BasketFinal() {
    }

    // ====== LOGIQUE MÉTIER ======

    public void addModuleGroup(Module module, Group group) {
        BasketEntry entry = new BasketEntry(this, module, group);
        entries.add(entry);
    }

    public Group getGroup(Module module) {
        return entries.stream()
                .filter(e -> e.getModule().equals(module))
                .map(BasketEntry::getGroup)
                .findFirst()
                .orElse(null);
    }

    public void removeModule(ModuleEntity module) {
        entries.removeIf(e -> e.getModule().equals(module));
    }

}