package fr.univtln.projet.planning.entity.planning;

import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    @Test
    public void testCreationCourse() {
        ProfessorEntity p1 = ProfessorEntity.ProfessorFactory("ElisAbetH", "MurisAsco");
        ProfessorEntity p2 = ProfessorEntity.ProfessorFactory("Valérie", "Gillot");

        ModuleEntity m = ModuleEntity.builder()
                .code("UE123")
                .name("developpement avancé")
                .ECTS(1)
                .responsible(p1)
                .build();

        Instant startTime = Instant.now();
        Duration duration = Duration.between(startTime, Instant.now());

        CourseEntity c = CourseEntity.builder()
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
