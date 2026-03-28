package fr.univtln.projet.planning;

// Imports de l'académique
import fr.univtln.projet.planning.service.ServiceRegistry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    
    @Override
    public void start(Stage stage) throws Exception {

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