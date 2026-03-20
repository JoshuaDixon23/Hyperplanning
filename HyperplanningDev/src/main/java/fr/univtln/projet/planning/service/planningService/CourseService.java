package fr.univtln.projet.planning.service.planningService;

import fr.univtln.projet.planning.entity.person.Professor;
import fr.univtln.projet.planning.entity.planning.Course;
import fr.univtln.projet.planning.entity.planning.CourseType;
import fr.univtln.projet.planning.entity.planning.Language;
import fr.univtln.projet.planning.entity.planning.Module;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseService {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Paris");

    public List<Course> getCoursesForWeek(LocalDate weekMonday) {
        return getCoursesBetween(weekMonday, weekMonday.plusDays(6));
    }

    public List<Course> getCoursesBetween(LocalDate startDate, LocalDate endDate) {
        List<Course> allCourses = buildFakeDatabaseCourses();
        List<Course> filtered = new ArrayList<>();

        for (Course course : allCourses) {
            LocalDate courseDate = LocalDateTime.ofInstant(course.getStartTime(), ZONE_ID).toLocalDate();

            if (!courseDate.isBefore(startDate) && !courseDate.isAfter(endDate)) {
                filtered.add(course);
            }
        }

        Collections.sort(filtered);
        return filtered;
    }

    private List<Course> buildFakeDatabaseCourses() {
        List<Course> courses = new ArrayList<>();

        Professor dupont = Professor.ProfessorFactory("Jean", "Dupont");
        Professor martin = Professor.ProfessorFactory("Claire", "Martin");

        Module informatique = Module.builder()
                .code("INFO101")
                .name("informatique")
                .language(Language.FRENCH)
                .ECTS(6)
                .responsible(dupont)
                .build();

        Module algorithmique = Module.builder()
                .code("ALGO101")
                .name("algorithmique")
                .language(Language.FRENCH)
                .ECTS(6)
                .responsible(martin)
                .build();

        Module projet = Module.builder()
                .code("PROJ201")
                .name("projet")
                .language(Language.FRENCH)
                .ECTS(4)
                .responsible(dupont)
                .build();

        Module mathematiques = Module.builder()
                .code("MATH101")
                .name("mathématiques")
                .language(Language.FRENCH)
                .ECTS(5)
                .responsible(dupont)
                .build();

        LocalDate referenceMonday = mondayOf(LocalDate.now());

        courses.add(buildCourse(informatique, referenceMonday, 8, 0, 120, CourseType.CM, dupont));
        courses.add(buildCourse(algorithmique, referenceMonday, 8, 30, 120, CourseType.TD, martin));
        courses.add(buildCourse(projet, referenceMonday.plusDays(2), 13, 30, 120, CourseType.TP, dupont));
        courses.add(buildCourse(mathematiques, referenceMonday.plusDays(4), 15, 0, 180, CourseType.CM, dupont));


        LocalDate week2 = referenceMonday.plusWeeks(1);
        courses.add(buildCourse(mathematiques, week2, 10, 0, 120, CourseType.CM, dupont));
        courses.add(buildCourse(projet, week2.plusDays(1), 14, 0, 120, CourseType.TP, dupont));
        courses.add(buildCourse(algorithmique, week2.plusDays(3), 9, 0, 120, CourseType.TD, martin));

        LocalDate week5 = referenceMonday.plusWeeks(4);
        courses.add(buildCourse(informatique, week5.plusDays(1), 8, 0, 180, CourseType.CM, dupont));
        courses.add(buildCourse(projet, week5.plusDays(3), 13, 30, 120, CourseType.TP, dupont));

        return courses;
    }

    private Course buildCourse(Module module, LocalDate date, int hour, int minute, long durationMinutes, CourseType courseType, Professor... professors) {

        Instant startTime = LocalDateTime.of(date, LocalTime.of(hour, minute))
                .atZone(ZONE_ID)
                .toInstant();

        Course.Builder builder = Course.builder()
                .module(module)
                .startTime(startTime)
                .duration(Duration.ofMinutes(durationMinutes))
                .courseType(courseType);

        if (professors != null) {
            for (Professor professor : professors) {
                if (professor != null) {
                    builder.professor(professor);
                }
            }
        }

        return builder.build();
    }

    private LocalDate mondayOf(LocalDate date) {
        int dow = date.getDayOfWeek().getValue();
        return date.minusDays(dow - 1L);
    }
}