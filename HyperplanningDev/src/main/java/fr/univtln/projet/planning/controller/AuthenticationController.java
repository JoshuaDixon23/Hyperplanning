
package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.modele.authentication.Authentication;
import fr.univtln.projet.planning.repository.authenticationRepository.AuthenticationRepository;
import fr.univtln.projet.planning.service.authenticationService.AuthenticationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.util.Optional;

/*
    Attention créer un entity manager à chaques fois peut etres pas cool (peut etre à optimiser)
 */

public class AuthenticationController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label planningLabel;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Button cancelButton;

    //classe utiles

    private AuthenticationService authenticationService;

    /**
     * Called automatically after FXML load
     */


    @FXML
    public void initialize() {
        planningLabel.setText("Planning 2026");

        errorLabel.setText("");
        errorLabel.setVisible(false);

        // liaison avec le service d'authentification
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        EntityManager em = emf.createEntityManager();
        AuthenticationRepository authRepo = new AuthenticationRepository(em);
        AuthenticationService authService = new AuthenticationService(em,authRepo);

        this.authenticationService = authService;

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

        if(!authenticationService.isEmailExiste(username)  ) {
            showError("Cet email n'existe pas: " + username);
            return;
        }

        // Simulation d'authentification
        if (authenticate(username, password)) {
            showSuccess("Connexion réussie : " + username);

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // !!! changer la scene en fonction et la rediction de l'authentification !!!
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //goToPlanning();


        } else {
            showError("Identifiants incorrects ");
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
        try {


            if (authenticationService.isPasswordDefined(username)) {
                    Optional<Authentication> auth = authenticationService.authenticate(username, password);
                    if (auth.isPresent()) {
                        // Connexion réussie
                        return true;
                    } else {
                        showError("Identifiants incorrects");
                        return false;
                    }
            } else {// la méthode passe par cette instruction si il n'y a pas de mdp
                    authenticationService.setPasswordFirstTime(username, password);
                    showSuccess("Mot de passe configuré");
                    // mot de passe défini pour la première fois, on considère que l'authentification est réussie
                    return true;
            }


        } catch (Exception e) {
            showError("Erreur lors de l'authentification: " + e.getMessage());
            return false;
        }
    }

    /**
     * Affichage erreur stylée
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setVisible(true);
    }



    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!! A modifier aussi                                             !!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private void goToPlanning() {
        try {
            // chemin
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

