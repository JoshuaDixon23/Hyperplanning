package fr.univtln.projet.planning.entity.planning;

import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import fr.univtln.projet.planning.modele.planning.CourseType;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

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

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Instant startTime = Instant.now();
        Duration duration = Duration.between(startTime, Instant.now().plusSeconds(60*60));

        CourseEntity c = CourseEntity.builder()
                .module(m)
                .date(date)
                .startTime(time)
                .duration(duration)
                .courseType(CourseType.CM)
                .professor(p1)
                .professor(p2)
                .build();

        System.out.println("Les professeurs : " + c.getProfessors());

        assertEquals(CourseType.CM, c.getCourseType());
        System.out.println(c.getState());

    }
}
