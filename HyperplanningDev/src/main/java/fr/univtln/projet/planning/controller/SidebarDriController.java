package fr.univtln.projet.planning.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarDriController {
    @FXML private HBox btnStudent;

    // Référence optionnelle vers le controller de la page courante
    // Settée uniquement depuis InternationalStudentUFRLanguageController
    private InternationalStudentUFRLanguageController ufrLanguageController;

    public void setUFRLanguageController(InternationalStudentUFRLanguageController ctrl) {
        this.ufrLanguageController = ctrl;
    }

    @FXML
    private void handleMenuClick() {
        // Si on est sur la page UFR/Language → pop-up d'avertissement
        if (ufrLanguageController != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Go back to students");
            confirm.setHeaderText("Are you sure you want to go back?");
            confirm.setContentText(
                    "Your current selections (UFR and languages) have not been saved\n"
                            + "and will be lost if you leave this page."
            );

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ufrLanguageController.navigateBackToHome();
                }
            });

        } else {
            // Sur la page d'accueil → "Student" ne fait rien de spécial
            // (on est déjà dessus)
        }
    }
}
