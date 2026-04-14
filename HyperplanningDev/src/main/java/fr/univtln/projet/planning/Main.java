package fr.univtln.projet.planning;

import fr.univtln.projet.planning.service.ServiceRegistry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ServiceRegistry.initialize();

        Parent root = FXMLLoader.load(
                getClass().getResource("/view/connexion-view.fxml")
        );

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