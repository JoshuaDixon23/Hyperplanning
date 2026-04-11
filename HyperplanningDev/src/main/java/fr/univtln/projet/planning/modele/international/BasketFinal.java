package fr.univtln.projet.planning.modele.international;

import java.util.ArrayList;
import java.util.List;

import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.person.InternationalStudent;
import fr.univtln.projet.planning.modele.planning.Module;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity
public class BasketFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketEntry> entries = new ArrayList<>();

    @OneToOne(mappedBy = "basketFinal", cascade = CascadeType.ALL)
    private InternationalStudent student;

    public BasketFinal() {}

    // Methods

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

    public void setStudent(InternationalStudent student) {
        this.student = student;
        if (student.getBasketFinal() != this) {
            student.setBasketFinal(this);
        }
    }
    //Avoid double
    public boolean containsModule(Module module) {
        return entries.stream()
                .anyMatch(e -> e.getModule().equals(module));
    }

    public boolean isValid(int maxEcts) {
        int total = entries.stream()
                .mapToInt(e -> (int) e.getModule().getEcts())
                .sum();

        return total <= maxEcts;
    }

    public void removeModule(ModuleEntity module) {
        entries.removeIf(e -> e.getModule().equals(module));
    }


    public String getModuleGroup() {
        return entries.toString();
    }
}