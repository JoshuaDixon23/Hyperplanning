package fr.univtln.projet.planning;

import java.util.List;
import java.util.stream.Collectors;

import fr.univtln.projet.planning.controller.DRIController;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.person.InternationalStudent;
import fr.univtln.projet.planning.service.ServiceRegistry;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ServiceRegistry.initialize();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dri-view.fxml"));
        Parent root = loader.load();

        DRIController controller = loader.getController();

        ModuleService moduleService = ServiceRegistry.getModuleService();
        List<ModuleEntity> firstThreeModules = moduleService.findAll().stream()
                .limit(3)
                .collect(Collectors.toList());

        InternationalStudent student = new InternationalStudent("Jean", "Dupont", "jean.dupont@etud.univ-tln.fr", "jean.dupont@gmail.com");

        controller.setCourseService(ServiceRegistry.getCourseService());
        controller.loadStudentDRI(student, firstThreeModules);

        Scene scene = new Scene(root, 1600, 900);

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

    public static void main(String[] args) {
        launch();
    }
}