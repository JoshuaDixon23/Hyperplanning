package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.entity.authentication.AuthenticationEntity;
import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.repository.personRepository.UserRepository;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.service.authenticationService.AuthenticationService;
import fr.univtln.projet.planning.service.planningService.CourseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
    private EntityManagerFactory emf;
    private EntityManager em;

    //classe utiles

    private AuthenticationService authenticationService;
    private UserRepository userRepository;

    /**
     * Called automatically after FXML load
     */


    @FXML
    public void initialize() {
        planningLabel.setText("Planning 2026");

        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        // liaison avec le service d'authentification
        emf = Persistence.createEntityManagerFactory("HyperplanningPU");
        em = emf.createEntityManager();
        UserRepository userRepo = new UserRepository(em);
        AuthenticationService authService = new AuthenticationService(em, userRepo);

        this.authenticationService = authService;
        this.userRepository = userRepo;


        // Entrée clavier = login
        usernameField.setOnAction(e -> handleLogin());
        passwordField.setOnAction(e -> handleLogin());

        // cas de la première authentification

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    if (authenticationService.isEmailExiste(newValue)) {
                        if (!authenticationService.isPasswordDefined(newValue)) {
                            errorLabel.setText("Première connexion :\n          le mot de passe sera défini avec votre saisie.");
                            errorLabel.setVisible(true);
                            errorLabel.setManaged(true);
                        } else {
                            errorLabel.setVisible(false);
                            errorLabel.setManaged(false);
                        }
                    } else {
                        errorLabel.setVisible(false);
                        errorLabel.setManaged(false);
                    }
                } catch (Exception e) {
                    errorLabel.setVisible(false);
                    errorLabel.setManaged(false);
                }
            } else {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
            }
        });





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

        Optional<User> optionalUser = userRepository.findByEmailUniv(username);
        // Simulation d'authentification
        if (authenticate(username, password)) {
            showSuccess("Connexion réussie : " + username);

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // !!! changer la scene en fonction et la rediction de l'authentification !!!
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //goToPlanning();

            User connectedUser = optionalUser.get();
            showSuccess("Connexion réussie : " + connectedUser.getFirstName() + " " + connectedUser.getLastName());

            goToPlanning(connectedUser);


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
        errorLabel.setManaged(false);
    }

    /**
     * Fake authentification (à remplacer par du vrai)
     */
    private boolean authenticate(String username, String password) {
        try {
            if (authenticationService.isPasswordDefined(username)) {
                Optional<AuthenticationEntity> auth = authenticationService.authenticate(username, password);
                if (auth.isPresent()) {
                    // Connexion réussie
                    return true;
                } else {
                    showError("Identifiants incorrects");
                    return false;
                }
            } else {
                // la méthode passe par cette instruction si il n'y a pas de mdp
                Optional<AuthenticationEntity> auth = authenticationService.setPasswordFirstTime(username, password);
                if (auth.isPresent()) {
                    showSuccess("Mot de passe configuré");
                    // mot de passe défini pour la première fois, on considère que l'authentification est réussie
                    return true;
                } else {
                    showError("Erreur lors de la configuration du mot de passe");
                    return false;
                }
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
        errorLabel.setManaged(true);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setManaged(true);
        errorLabel.setVisible(true);
    }



    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!! A modifier aussi                                             !!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private void goToPlanning(User connectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/planning-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().addAll(
                    getClass().getResource("/css/base.css").toExternalForm(),
                    getClass().getResource("/css/sidebar.css").toExternalForm(),
                    getClass().getResource("/css/components.css").toExternalForm(),
                    getClass().getResource("/css/planning.css").toExternalForm()
            );

            PlanningController planningController = loader.getController();

            CourseRepository courseRepository = new CourseRepository(em);
            CourseService courseService = new CourseService(courseRepository);

            planningController.setCourseService(courseService);
            planningController.setConnectedUser(connectedUser);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du chargement du planning : " + e.getMessage());
        }
    }

}

