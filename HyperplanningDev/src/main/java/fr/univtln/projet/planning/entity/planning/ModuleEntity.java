package fr.univtln.projet.planning.entity.planning;

import fr.univtln.projet.planning.entity.TextTransformation;
import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import fr.univtln.projet.planning.modele.planning.Language;
import fr.univtln.projet.planning.modele.planning.CourseType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter

public class ModuleEntity {
    private String code; // code may be not unique and depend on promo ?, a dictionary may be a solution
    private String name;
    private Language language;
    private float ECTS; // number of credits
    private ProfessorEntity responsible;
    private Map<CourseType, Float> courseHours; // Dictionary for CM/TD/TP hours in module

    public ModuleEntity(String b){
        code = b.code;
        name = b.name;
        language = b.language;
        ECTS = b.ECTS;
        responsible = b.responsible;
        courseHours = new HashMap<CourseType, Float>();
    }

    public String code() { return code; }

    public String name() { return name; }

    public Language language() { return language; }

    public float ECTS() { return ECTS; }

    public ProfessorEntity responsible() { return responsible; }

    public static Builder builder() { return new Builder(); }

    public ProfessorEntity getResponsible() {
        return responsible;
    }

    public static final class Builder {
        private String code = "";
        private String name = "";
        private Language language = Language.FRENCH;
        private float ECTS = 0;
        private ProfessorEntity responsible = null;
        // the representation of number of hours for CM, TD, TP predefined to think about

        public Builder() {
        }

        public Builder code(String c) { this.code = c.toUpperCase(); return this; }

        public Builder name(String n) { this.name = TextTransformation.capitalize(n); return this; }

        public Builder language(Language l) { this.language = l; return this; }

        public Builder ECTS(float ec)  { this.ECTS = ec; return this; }

        public Builder responsible(ProfessorEntity p) { this.responsible = p; return this; }

        public ModuleEntity build() {
            Objects.requireNonNull(name, "name required");
            Objects.requireNonNull(ECTS, "ECTS required");

            return new ModuleEntity(this);
        }
    }

    @Override
    public String toString() {
        return "ModuleEntity{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", language=" + language +
                ", ECTS=" + ECTS +
                ", responsible=" + responsible +
                ", courseHours=" + courseHours +
                '}';
    }
}
