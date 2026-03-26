package fr.univtln.projet.planning.modele.international;

import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
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

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private InternationalStudentEntity internationalStudent;

    @ManyToMany
    @JoinTable(
            name = "basket_module_modules",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<ModuleEntity> modules = new HashSet<ModuleEntity>();

    // Constructeur vide obligatoire
    public BasketModule() {}

    public BasketModule(InternationalStudentEntity internationalStudent) {
        this.internationalStudent = internationalStudent;
    }

    public void addModule(ModuleEntity module) {
        modules.add(module);
    }

    public void removeModule(ModuleEntity module) {
        modules.remove(module);
    }

    public void setInternationalStudent(InternationalStudentEntity student) {
    }
}