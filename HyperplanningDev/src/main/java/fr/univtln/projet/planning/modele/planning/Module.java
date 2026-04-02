package fr.univtln.projet.planning.modele.planning;

import java.util.ArrayList; // Adjust package if needed
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.univtln.projet.planning.entity.TextTransformation;
import fr.univtln.projet.planning.modele.academic.Group;
import fr.univtln.projet.planning.modele.person.Professor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Modules")
@Getter
@Setter
public class Module {

    @Id
    @Column(name = "code", length = 20, unique = true)
    private String code; 

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Column(nullable = false)
    private float ects;

    @ManyToOne
    @JoinColumn(name = "responsibleId")
    private Professor responsible;

    @ManyToMany(mappedBy = "modules")
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> planning = new ArrayList<>();
    
    protected Module() {
    }

    private Module(Builder b) {
        this.code = b.code;
        this.name = b.name;
        this.language = b.language;
        this.ects = b.ects;
        this.responsible = b.responsible;
        //this.planning = b.courses;
    }

    /*
    public void addCourse(Course course) {
        planning.add(course);
        course.setModule(this);
    }

    public void removeCourse(Course course) {
        planning.remove(course);
        course.setModule(null);
    }

     */

    @PreRemove
    private void removeGroupsAssociations() {
        for (Group group : this.groups) { 
            group.getModules().remove(this);
        }   
    }


    public static Builder builder() { 
        return new Builder(); 
    }

    public static final class Builder {
        private String code = null;
        private String name = null;
        private Language language = Language.FRENCH; // Default value
        private float ects = 0;
        private Professor responsible = null;
        private List<Course> courses = new ArrayList<>();

        public Builder() {}

        public Builder code(String c) { 
            if (c != null) this.code = c.toUpperCase(); 
            return this; 
        }

        public Builder name(String n) { 
            if (n != null) this.name = TextTransformation.capitalize(n); 
            return this; 
        }

        public Builder language(Language l) { 
            if (l != null) this.language = l; 
            return this; 
        }

        public Builder ects(float ec)  { 
            this.ects = ec; 
            return this; 
        }

        public Builder responsible(Professor p) { 
            this.responsible = p; 
            return this; 
        }

        /*
        public Builder addCourse(Course c) {
            this.courses.add(c);
            return this;
        }
         */

        public Module build() {
            Objects.requireNonNull(code, "code is required for the database ID");
            Objects.requireNonNull(name, "name is required");
            if (ects <= 0) {
                throw new IllegalArgumentException("ECTS must be strictly positive");
            }

            return new Module(this);
        }
    }

    @Override
    public String toString() {
        return "Module{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", language=" + language +
                ", ects=" + ects +
                ", responsible=" + responsible +
                '}';
    }

    public Set<Group> getGroups() {
        return groups;
    }
}