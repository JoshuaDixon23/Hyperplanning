package fr.univtln.projet.planning.modele.international;

import fr.univtln.projet.planning.modele.planning.Module;
import fr.univtln.projet.planning.modele.person.InternationalStudent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "basket_module")
public class BasketModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private InternationalStudent internationalStudent;

    @ManyToMany
    @JoinTable(
            name = "basket_module_modules",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<Module> modules = new HashSet<>();

    // Constructeur vide obligatoire
    public BasketModule() {}

    public BasketModule(InternationalStudent internationalStudent) {
        this.internationalStudent = internationalStudent;
    }

    public void addModule(Module module) {
        modules.add(module);
    }

    public void removeModule(Module module) {
        modules.remove(module);
    }

    public void setInternationalStudent(InternationalStudent student) {
    }
}