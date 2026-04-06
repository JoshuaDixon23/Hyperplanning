package fr.univtln.projet.planning;

import fr.univtln.projet.planning.service.ServiceRegistry;
import fr.univtln.projet.planning.service.planningService.CourseService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private CourseService courseService;

    @Override
    public void start(Stage stage) throws Exception {
        ServiceRegistry.initialize();

        Parent root = FXMLLoader.load(
                getClass().getResource("/view/staffDri-student-view.fxml")
        );

        Scene scene = new Scene(root, 900, 700);

        scene.getStylesheets().addAll(
                getClass().getResource("/css/base.css").toExternalForm(),
                getClass().getResource("/css/sidebar.css").toExternalForm(),
                getClass().getResource("/css/components.css").toExternalForm(),
                getClass().getResource("/css/planning.css").toExternalForm(),
                getClass().getResource("/css/connexion.css").toExternalForm()
        );

        stage.setTitle("Hyperplanning");
        stage.setMinWidth(980);
        stage.setMinHeight(720);
        stage.setScene(scene);
         stage.setOnCloseRequest(event -> {
            ServiceRegistry.shutdown();
        });

        stage.show();

       
    }


    /* voici le main de la branch dev

    // Initialize all services at application startup
        ServiceRegistry.initialize();

        Parent root = FXMLLoader.load(
                getClass().getResource("/view/admin-course-view.fxml")
        );

        Scene scene = new Scene(root, 1200, 800);

        scene.getStylesheets().addAll(
                getClass().getResource("/css/base.css").toExternalForm(),
                getClass().getResource("/css/sidebar.css").toExternalForm(),
                getClass().getResource("/css/components.css").toExternalForm(),
                getClass().getResource("/css/planning.css").toExternalForm()
        );

        stage.setTitle("Hyperplanning");
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.setScene(scene);

        // Graceful shutdown: close ServiceRegistry when window closes
        stage.setOnCloseRequest(event -> {
            ServiceRegistry.shutdown();
        });

        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}


     */



    /*
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(
                getClass().getResource("/view/planning-view.fxml")
        );

        Scene scene = new Scene(root, 1200, 800);

        scene.getStylesheets().addAll(
                getClass().getResource("/css/base.css").toExternalForm(),
                getClass().getResource("/css/sidebar.css").toExternalForm(),
                getClass().getResource("/css/components.css").toExternalForm(),
                getClass().getResource("/css/planning.css").toExternalForm()
        );

        stage.setTitle("Hyperplanning");
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();
    }

     */

    public static void main(String[] args) {

        launch();
        /*
        System.out.println("⏳ Starting Hibernate and connecting to the database...");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.println("\n=====================================================");
            System.out.println("🌍 THE ULTIMATE TEST: CREATING THE UNIVERSITY");
            System.out.println("=====================================================");

            em.getTransaction().begin();

            // ==========================================
            // 1. PEOPLE (Users - Sans InternationalStudent)
            // ==========================================
            System.out.println("1️⃣ Creating Users...");
            Admin admin = new Admin("jean", "michel", "jean.michel7@univ-tln.fr");
            Professor prof = new Professor("alan", "turing", "alan.turing3@univ-tln.fr");
            LocalStudent localStudent = new LocalStudent("alice", "liddell", "alice-perso621@etud.univ-tln.fr", "alice.perso@gmail.com");

            em.persist(admin);
            em.persist(prof);
            em.persist(localStudent);

            // ==========================================
            // 2. INFRASTRUCTURE (Campus -> Building -> Room)
            // ==========================================
            System.out.println("2️⃣ Creating Infrastructure...");
            Campus campus = Campus.CampusFactory("La Garde", "map_lagarde.png");

            Map<Day, Building.Hours> hoursMap = new EnumMap<>(Day.class);
            hoursMap.put(Day.MONDAY, new Building.Hours(LocalTime.of(8, 0), LocalTime.of(20, 0)));

            Building buildingU = Building.BuildingFactory("Bâtiment U", "Nord", hoursMap);
            campus.addBuilding(buildingU);

            Room roomInfo = Room.RoomFactory("U014", 30, RoomType.INFO, buildingU);
            buildingU.addRoom(roomInfo);

            em.persist(campus);

            // ==========================================
            // 3. ACADEMIC (UFR -> Promo -> Group <-> Student)
            // ==========================================
            System.out.println("3️⃣ Creating Academic Tree...");
            UFR ufr = UFR.UFRFactory("UFR Sciences", campus, admin);
            campus.addUfr(ufr);

            Promo promoL3 = Promo.PromoFactory("Licence Informatique", 2026, StudyLevel.L3, ufr);
            ufr.addPromo(promoL3);

            Group groupPromo = Group.GroupFactory(1, GroupType.PROMO);
            Group groupTP = Group.GroupFactory(1, GroupType.TP);

            promoL3.addGroup(groupPromo);
            promoL3.addGroup(groupTP);

            promoL3.addStudent(localStudent);
            localStudent.addGroup(groupPromo);
            localStudent.addGroup(groupTP);

            em.persist(ufr);

            // ==========================================
            // 4. PLANNING (Module -> Course <-> Group)
            // ==========================================

            Duration duration = Duration.ofHours(2);
            System.out.println("4️⃣ Creating Planning...");
            Module javaModule = Module.builder()
                    .code("m-java-01")
                    .name("programmation orientée objet")
                    .language(Language.FRENCH)
                    .ects(6.0f)
                    .responsible(prof)
                    .build();

            Course courseJavaTP = Course.builder()
                    .module(javaModule)
                    .date(LocalDate.of(2026, 9, 15))
                    .startTime(LocalTime.of(10, 0))
                    .duration(duration)
                    .courseType(CourseType.TP)
                    .room(roomInfo)
                    .professor(prof)
                    .build();

            javaModule.addCourse(courseJavaTP);
            groupTP.addCourse(courseJavaTP);

            em.persist(javaModule);

            // Commit final
            em.getTransaction().commit();
            System.out.println("✅ All data successfully saved to NeonDB!");

            // ==========================================
            // 5. VERIFICATION (Read from DB)
            // ==========================================
            System.out.println("\n=====================================================");
            System.out.println("📊 VERIFYING THE DATABASE");
            System.out.println("=====================================================");
            em.clear();

            LocalStudent savedStudent = em.createQuery(
                            "SELECT s FROM LocalStudent s WHERE s.firstName = 'Alice'", LocalStudent.class)
                    .getSingleResult();

            System.out.println("\n🎓 ALICE'S ACADEMIC PROFILE:");
            System.out.println("  Promo: " + savedStudent.getPromo().getName() + " (" + savedStudent.getPromo().getStudyLevel() + ")");

            for (Group g : savedStudent.getGroups()) {
                System.out.println("    └ Group: " + g.getType() + " " + g.getNum());
                for (Course c : g.getPlanning()) {
                    System.out.println("       └ Course: " + c.getModule().getName() + " (Room " + c.getRoom().getNumber() + ")");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR: A problem occurred during execution.");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

         */
    }
}