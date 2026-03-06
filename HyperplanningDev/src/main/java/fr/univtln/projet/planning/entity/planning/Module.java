package fr.univtln.projet.planning.entity.planning;

import fr.univtln.projet.planning.entity.TextTransformation;
import fr.univtln.projet.planning.entity.person.Professor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Module {
    private String code; // code may be not unique and depend on promo ?, a dictionary may be a solution
    private String name;
    private Language language;
    private float ECTS; // number of credits
    private Professor responsible;
    private Map<CourseType, Float> courseHours; // Dictionary for CM/TD/TP hours in module

    // private Set<Group> groups;

    private Module(Builder b){
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

    public Professor responsible() { return responsible; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String code = "";
        private String name = "";
        private Language language = Language.FRENCH;
        private float ECTS = 0;
        private Professor responsible = null;
        // the representation of number of hours for CM, TD, TP predefined to think about

        public Builder() {
        }

        public Builder code(String c) { this.code = c.toUpperCase(); return this; }

        public Builder name(String n) { this.name = TextTransformation.capitalize(n); return this; }

        public Builder language(Language l) { this.language = l; return this; }

        public Builder ECTS(float ec)  { this.ECTS = ec; return this; }

        public Builder responsible(Professor p) { this.responsible = p; return this; }

        public Module build() {
            Objects.requireNonNull(name, "name required");
            Objects.requireNonNull(ECTS, "ECTS required");

            return new Module(this);
        }
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Language getLanguage() {
        return language;
    }

    public float getECTS() {
        return ECTS;
    }

    public void setCourseHours(Map<CourseType, Float> courseHours) {
        this.courseHours = courseHours;
    }
}
