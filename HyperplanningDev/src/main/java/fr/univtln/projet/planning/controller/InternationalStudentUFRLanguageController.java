package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.entity.academic.UFREntity;
import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.modele.planning.Language;
import fr.univtln.projet.planning.service.ServiceRegistry;
import fr.univtln.projet.planning.service.academicService.UFRService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InternationalStudentUFRLanguageController implements Initializable {

    // -------------------------------------------------------------------------
    // FXML injections
    // -------------------------------------------------------------------------

    @FXML private Label studentNameLabel;
    @FXML private VBox ufrListContainer;
    @FXML private VBox languageListContainer;
    @FXML private SidebarDriController sidebarController;

    // -------------------------------------------------------------------------
    // Données
    // -------------------------------------------------------------------------

    private InternationalStudentEntity currentStudent;

    private UFRService ufrService;

    // Sélections courantes
    private final List<UFREntity> selectedUFRs = new ArrayList<>();
    private final List<Language> selectedLanguages = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ufrService = ServiceRegistry.getUFRService();
        } catch (IllegalStateException e) {
            System.err.println("ServiceRegistry not initialized: " + e.getMessage());
            return;
        }
        // Les listes sont peuplées après injection de l'étudiant via setStudent()
    }

    /**
     * Point d'entrée : appelé par InternationalStudentController
     * juste après le chargement du FXML via FXMLLoader.
     */
    public void setStudent(InternationalStudentEntity student) {
        this.currentStudent = student;
        studentNameLabel.setText(
                student.getFirstName() + " " + student.getLastName().toUpperCase()
        );

        // TODO: vérifier ici si un ModuleBasket existe déjà pour cet étudiant.
        //       Si oui, rediriger directement vers la page de planning.
        //       ex: if (moduleBasketService.existsByStudent(student)) { navigateToPlanning(); return; }


        if (sidebarController != null) {
            sidebarController.setUFRLanguageController(this);
        }

        loadUFRs();
        loadLanguages();
    }

    // -------------------------------------------------------------------------
    // Chargement des listes
    // -------------------------------------------------------------------------

    private void loadUFRs() {
        ufrListContainer.getChildren().clear();
        selectedUFRs.clear();

        try {
            List<UFREntity> allUFRs = ufrService.findAll();

            if (allUFRs.isEmpty()) {
                Label empty = new Label("Aucun UFR disponible en base.");
                empty.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
                ufrListContainer.getChildren().add(empty);
                return;
            }

            for (UFREntity ufr : allUFRs) {
                CheckBox cb = new CheckBox(ufr.getName());
                cb.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");
                cb.setPadding(new Insets(4, 0, 4, 0));
                cb.setMaxWidth(Double.MAX_VALUE);

                cb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        selectedUFRs.add(ufr);
                    } else {
                        selectedUFRs.remove(ufr);
                    }
                });

                ufrListContainer.getChildren().add(cb);
            }

        } catch (Exception e) {
            System.err.println("Erreur chargement UFRs : " + e.getMessage());
            showErrorAlert("Erreur", "Impossible de charger les UFR depuis la base de données.");
        }
    }

    private void loadLanguages() {
        languageListContainer.getChildren().clear();
        selectedLanguages.clear();

        for (Language lang : Language.values()) {
            CheckBox cb = new CheckBox(lang.name());
            cb.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");
            cb.setPadding(new Insets(4, 0, 4, 0));

            cb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedLanguages.add(lang);
                } else {
                    selectedLanguages.remove(lang);
                }
            });

            languageListContainer.getChildren().add(cb);
        }
    }

    // -------------------------------------------------------------------------
    // Handler Valider
    // -------------------------------------------------------------------------

    @FXML
    private void handleValidate() {

        // --- Validation ---
        if (selectedUFRs.isEmpty() && selectedLanguages.isEmpty()) {
            showErrorAlert(
                    "Sélection vide",
                    "Veuillez sélectionner au moins un UFR et une langue avant de continuer."
            );
            return;
        }

        if (selectedUFRs.isEmpty()) {
            showErrorAlert(
                    "UFR manquant",
                    "Veuillez sélectionner au moins un UFR."
            );
            return;
        }

        if (selectedLanguages.isEmpty()) {
            showErrorAlert(
                    "Langue manquante",
                    "Veuillez sélectionner au moins une langue d'enseignement."
            );
            return;
        }

        // --- Navigation vers la page suivante ---
        try {
            // TODO: remplacer le chemin par votre vrai FXML de la page suivante
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/student-module-selection.fxml")
            );
            Parent root = loader.load();

            // TODO: créer StudentModuleSelectionController et y ajouter une méthode :
            //       public void setContext(InternationalStudentEntity student,
            //                             List<UFREntity> ufrs,
            //                             List<Language> languages)
            // StudentModuleSelectionController next = loader.getController();
            // next.setContext(currentStudent, selectedUFRs, selectedLanguages);

            Stage stage = (Stage) studentNameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            System.err.println("Erreur navigation : " + e.getMessage());
            showErrorAlert("Erreur", "Impossible d'ouvrir la page suivante.");
        }
    }

    public void navigateBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/staffDri-student-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) studentNameLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            showErrorAlert("Error", "Unable to return to the students page.");
        }
    }

    // -------------------------------------------------------------------------
    // Utilitaires
    // -------------------------------------------------------------------------

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}