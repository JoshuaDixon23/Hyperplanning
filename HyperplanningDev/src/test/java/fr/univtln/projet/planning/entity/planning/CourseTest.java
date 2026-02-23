package fr.univtln.projet.planning.entity.planning;

import fr.univtln.projet.planning.entity.person.Professor;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    @Test
    public void testCreationCourse() {
        Professor p1 = Professor.ProfessorFactory("ElisAbetH", "MurisAsco");
        Professor p2 = Professor.ProfessorFactory("Valérie", "Gillot");

        Module m = Module.builder()
                .code("UE123")
                .name("developpement avancé")
                .ECTS(1)
                .responsible(p1)
                .build();

        Instant startTime = Instant.now();
        Duration duration = Duration.between(startTime, Instant.now());

        Course c = Course.builder()
                .module(m)
                .startTime(startTime)
                .duration(duration)
                .courseType(CourseType.CM)
                .professor(p1)
                .professor(p2)
                .build();

        System.out.println("Les professeurs : " + c.getProfessors());

        assertEquals(CourseType.CM, c.getCourseType());

    }
}
