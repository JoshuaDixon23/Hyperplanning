
package fr.univtln.projet.planning.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class ConnexionController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label planningLabel;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Button cancelButton;

    /**
     * Called automatically after FXML load
     */
    @FXML
    public void initialize() {
        planningLabel.setText("Planning 2026");

        errorLabel.setText("");
        errorLabel.setVisible(false);

        // Entrée clavier = login
        usernameField.setOnAction(e -> handleLogin());
        passwordField.setOnAction(e -> handleLogin());
    }

    /**
     * Action bouton "Valider"
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validation simple
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        // Simulation d'authentification
        if (authenticate(username, password)) {
            System.out.println("Connexion réussie : " + username);

            // 👉 ici tu peux changer de scène (planning)
            goToPlanning();

        } else {
            showError("Identifiants incorrects");
        }
    }

    /**
     * Action bouton "Annuler"
     */
    @FXML
    private void handleCancel() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setVisible(false);
    }

    /**
     * Fake authentification (à remplacer par du vrai)
     */
    private boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("1234");
    }

    /**
     * Affichage erreur stylée
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(true);
    }

    /**
     * Navigation vers planning
     */
    private void goToPlanning() {
        try {
            // ⚠️ adapte le chemin
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/fxml/planning.fxml")
            );

            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

            javafx.stage.Stage stage = (javafx.stage.Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du chargement du planning");
        }
    }
}