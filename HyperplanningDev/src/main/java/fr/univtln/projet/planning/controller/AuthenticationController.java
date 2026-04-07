package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.modele.person.*;
import fr.univtln.projet.planning.repository.academicRepository.PromoRepository;
import fr.univtln.projet.planning.repository.academicRepository.UFRRepository;
import fr.univtln.projet.planning.repository.infrastructureRepository.CampusRepository;
import fr.univtln.projet.planning.repository.personRepository.AdminRepository;
import fr.univtln.projet.planning.repository.personRepository.UserRepository;
import fr.univtln.projet.planning.repository.planningRepository.CourseRepository;
import fr.univtln.projet.planning.service.academicService.PromoService;
import fr.univtln.projet.planning.service.academicService.UFRService;
import fr.univtln.projet.planning.service.authenticationService.AuthenticationService;
import fr.univtln.projet.planning.service.infrastructureService.CampusService;
import fr.univtln.projet.planning.service.personService.AdminService;
import fr.univtln.projet.planning.service.planningService.CourseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
    @FXML private VBox confirmPasswordBox;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button guestButton;

    private EntityManagerFactory emf;
    private EntityManager em;

    //classe utiles

    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private boolean firstConnectionMode = false;

    /**
     * Called automatically after FXML load
     */



    @FXML
    public void initialize() {
        planningLabel.setText("Planning 2026");

        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        confirmPasswordBox.setVisible(false);
        confirmPasswordBox.setManaged(false);
        firstConnectionMode = false;

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
        confirmPasswordField.setOnAction(e -> handleLogin());

        // cas de la première authentification
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                try {
                    if (authenticationService.isEmailExiste(newValue)) {
                        if (!authenticationService.isPasswordDefined(newValue)) {

                            firstConnectionMode = true;

                            confirmPasswordBox.setVisible(true);
                            confirmPasswordBox.setManaged(true);

                            errorLabel.setText("Première connexion : veuillez créer votre mot de passe");
                            errorLabel.setTextFill(Color.DARKBLUE);
                            errorLabel.setVisible(true);
                            errorLabel.setManaged(true);

                        } else {

                            firstConnectionMode = false;

                            confirmPasswordField.clear();
                            confirmPasswordBox.setVisible(false);
                            confirmPasswordBox.setManaged(false);

                            errorLabel.setVisible(false);
                            errorLabel.setManaged(false);
                        }
                    } else {
                        firstConnectionMode = false;

                        confirmPasswordField.clear();
                        confirmPasswordBox.setVisible(false);
                        confirmPasswordBox.setManaged(false);

                        errorLabel.setVisible(false);
                        errorLabel.setManaged(false);
                    }
                } catch (Exception e) {
                    firstConnectionMode = false;

                    confirmPasswordField.clear();
                    confirmPasswordBox.setVisible(false);
                    confirmPasswordBox.setManaged(false);

                    errorLabel.setVisible(false);
                    errorLabel.setManaged(false);
                }
            } else {
                firstConnectionMode = false;

                confirmPasswordField.clear();
                confirmPasswordBox.setVisible(false);
                confirmPasswordBox.setManaged(false);

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

        if(! authenticationService.isValidEmail(username)){
            showError("Cet email est invalide: " + username);
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        if(!authenticationService.isEmailExiste(username)  ) {
            showError("Cet email n'existe pas: " + username);
            return;
        }

        if (firstConnectionMode) {
            String confirmPassword = confirmPasswordField.getText();

            if (confirmPassword == null || confirmPassword.isEmpty()) {
                showError("Veuillez confirmer votre mot de passe");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Les mots de passe ne correspondent pas");
                return;
            }

            if (!authenticationService.isValidPassword(password)) {
                showError("Le mot de passe doit contenir 8 caractère dont 1 spécial");
                return;

            }
        }


        Optional<User> optionalUser = authenticationService.findUserByEmail(username);

        // Simulation d'authentification
        if (authenticate(username, password)) {
            showSuccess("Connexion réussie : " + username);

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // !!! changer la scene en fonction et la rediction de l'authentification !!!
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //goToPlanning();

            User connectedUser = optionalUser.get();
            showSuccess("Connexion réussie : " + connectedUser.getFirstName() + " " + connectedUser.getLastName());
            
            //if (connectedUser instanceof Admin) {
            //    System.out.println("je suis un admin");
            //}
//



            redirectUser(connectedUser);




        } else {
            showError("Identifiants incorrects ");
        }
    }

    @FXML
    private void handleGuestAccess() {
        goToGuestPlanning();
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

                Optional<User> userOpt = authenticationService.authenticate(username, password);
                if (userOpt.isPresent()) {
                    return true;
                } else {
                    showError("Identifiants incorrects");
                    return false;
                }

            } else {

                Optional<User> userOpt = authenticationService.setPasswordFirstTime(username, password);
                if (userOpt.isPresent()) {
                    showSuccess("Mot de passe configuré");
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


    private void goToGuestPlanning() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/main-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().addAll(
                    getClass().getResource("/css/base.css").toExternalForm(),
                    getClass().getResource("/css/sidebar.css").toExternalForm(),
                    getClass().getResource("/css/components.css").toExternalForm(),
                    getClass().getResource("/css/planning.css").toExternalForm()
            );

            MainController mainController = loader.getController();

            CourseRepository courseRepository = new CourseRepository(em);
            CourseService courseService = new CourseService(courseRepository);

            PromoRepository promoRepository = new PromoRepository(em);
            UFRRepository ufrRepository = new UFRRepository(em);
            AdminRepository adminRepository = new AdminRepository(em);
            CampusRepository campusRepository = new CampusRepository(em);

            AdminService adminService = new AdminService(adminRepository);
            CampusService campusService = new CampusService(campusRepository);
            UFRService ufrService = new UFRService(ufrRepository, adminService, campusService);
            PromoService promoService = new PromoService(promoRepository, ufrService);

            mainController.setCourseService(courseService);
            mainController.setPromoService(promoService);
            mainController.setGuestMode(true);
            mainController.initGuestData();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du chargement du planning invité : " + e.getMessage());
        }
    }

    private void goToPlanning(User connectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/main-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().addAll(
                    getClass().getResource("/css/base.css").toExternalForm(),
                    getClass().getResource("/css/sidebar.css").toExternalForm(),
                    getClass().getResource("/css/components.css").toExternalForm(),
                    getClass().getResource("/css/planning.css").toExternalForm()
            );

            MainController mainController = loader.getController();

            CourseRepository courseRepository = new CourseRepository(em);
            CourseService courseService = new CourseService(courseRepository);

            PromoRepository promoRepository = new PromoRepository(em);
            UFRRepository ufrRepository = new UFRRepository(em);
            AdminRepository adminRepository = new AdminRepository(em);
            CampusRepository campusRepository = new CampusRepository(em);

            AdminService adminService = new AdminService(adminRepository);
            CampusService campusService = new CampusService(campusRepository);
            UFRService ufrService = new UFRService(ufrRepository, adminService, campusService);
            PromoService promoService = new PromoService(promoRepository, ufrService);

            mainController.setCourseService(courseService);
            mainController.setPromoService(promoService);
            mainController.setConnectedUser(connectedUser);
            mainController.initData();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du chargement du planning : " + e.getMessage());
        }
    }

    private void goToAdminInterface(User connectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/admin-course-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().addAll(
                    getClass().getResource("/css/base.css").toExternalForm(),
                    getClass().getResource("/css/sidebar.css").toExternalForm(),
                    getClass().getResource("/css/components.css").toExternalForm()
            );

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de l'interface admin : " + e.getMessage());
        }
    }

    private void goToStudentPlanning(User connectedUser) {
        goToPlanning(connectedUser);
    }

    private void goToProfessorPlanning(User connectedUser) {
        goToPlanning(connectedUser);
    }


    private void redirectUser(User connectedUser) {
        if (connectedUser instanceof Admin) {
            goToAdminInterface(connectedUser);
        } else if (connectedUser instanceof Professor) {
            goToProfessorPlanning(connectedUser);
        } else if (connectedUser instanceof LocalStudent) {
            goToStudentPlanning(connectedUser);
        } else {
            showError("Rôle utilisateur non reconnu");
        }
    }

}

