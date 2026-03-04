package fr.univtln.projet.planning.entity.academic;
import fr.univtln.projet.planning.entity.person.Student;
import fr.univtln.projet.planning.entity.planning.Planning;
import fr.univtln.projet.planning.entity.planning.Module;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Group {

    private GroupType type;
    private final Planning planning;
    private Promo promo;

    private final Set<Module> modules = new HashSet<>();
    private final Set<Student> students = new HashSet<>();

    



}
