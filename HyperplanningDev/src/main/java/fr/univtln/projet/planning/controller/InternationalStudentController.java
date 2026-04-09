package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.entity.person.InternationalStudentEntity;
import fr.univtln.projet.planning.service.personService.InternationalStudentService;
import fr.univtln.projet.planning.service.ServiceRegistry;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class InternationalStudentController implements Initializable {

    // -------------------------------------------------------------------------
    // FXML injections
    // -------------------------------------------------------------------------

    @FXML private GridPane studentsGrid;
    @FXML private TextField searchField;
    @FXML private Label pageIndicator;

    // -------------------------------------------------------------------------
    // Pagination
    // -------------------------------------------------------------------------

    private static final int CARDS_PER_PAGE = 9; // 3 colonnes × 3 lignes
    private int currentPage = 0;

    // -------------------------------------------------------------------------
    // Données
    // -------------------------------------------------------------------------

    private List<InternationalStudentEntity> allStudents = new ArrayList<>();
    private List<InternationalStudentEntity> filteredStudents = new ArrayList<>();

    private InternationalStudentService studentService;

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // TODO: ajouter getInternationalStudentService() dans ServiceRegistry
            //       comme ton collègue a getRoomService(), getModuleService(), etc.
            studentService = ServiceRegistry.getInternationalStudentService();
        } catch (IllegalStateException e) {
            System.err.println("ServiceRegistry not initialized: " + e.getMessage());
            return;
        }

        loadStudents();
        setupSearch();
    }

    // -------------------------------------------------------------------------
    // Chargement
    // -------------------------------------------------------------------------

    /**
     * Charge tous les étudiants depuis le service (première page uniquement
     * pour le cache initial, puis on pagine côté liste locale).
     *
     * REMARQUE : ton service expose findAll(pageNumber, pageSize) sans findAll()
     * global. Deux options :
     *
     *   Option A (recommandée) — ajouter dans InternationalStudentService :
     *       public List<InternationalStudentEntity> findAll() {
     *           return internationalStudentRepository.findAll()
     *                   .stream()
     *                   .map(InternationalStudentMapper::toDomain)
     *                   .toList();
     *       }
     *   puis appeler studentService.findAll() ici.
     *
     *   Option B — garder la pagination côté service et charger par blocs.
     *   Dans ce cas remplacer le bloc ci-dessous par une boucle sur findAll(page, size).
     */
    private void loadStudents() {
        // TODO: remplacer par studentService.findAll() une fois la méthode ajoutée
        //       (voir Option A dans le commentaire ci-dessus)
        allStudents = studentService.findAll(0, Integer.MAX_VALUE);

        filteredStudents = new ArrayList<>(allStudents);
        currentPage = 0;
        displayPage();
    }

    // -------------------------------------------------------------------------
    // Recherche en temps réel
    // -------------------------------------------------------------------------

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String query = (newValue == null) ? "" : newValue.trim().toLowerCase();

            if (query.isEmpty()) {
                filteredStudents = new ArrayList<>(allStudents);
            } else {
                filteredStudents = allStudents.stream()
                        .filter(s -> matchesSearch(s, query))
                        .collect(Collectors.toList());
            }

            currentPage = 0;
            displayPage();
        });
    }

    private boolean matchesSearch(InternationalStudentEntity student, String query) {
        String fullName = (student.getFirstName() + " " + student.getLastName()).toLowerCase();
        return fullName.contains(query);
    }

    // -------------------------------------------------------------------------
    // Affichage de la grille
    // -------------------------------------------------------------------------

    private void displayPage() {
        studentsGrid.getChildren().clear();

        int fromIndex = currentPage * CARDS_PER_PAGE;
        int toIndex   = Math.min(fromIndex + CARDS_PER_PAGE, filteredStudents.size());

        if (fromIndex >= filteredStudents.size() && currentPage > 0) {
            currentPage = 0;
            fromIndex = 0;
            toIndex = Math.min(CARDS_PER_PAGE, filteredStudents.size());
        }

        List<InternationalStudentEntity> pageStudents = filteredStudents.subList(fromIndex, toIndex);

        if (pageIndicator != null) {
            pageIndicator.setText("Chargement...");
        }

        // Création des cartes sur le Thread UI (identique au pattern de ton collègue)
        for (int i = 0; i < pageStudents.size(); i++) {
            Button card = createStudentCard(pageStudents.get(i));
            int col = i % 3;
            int row = i / 3;
            GridPane.setMargin(card, new Insets(5));
            GridPane.setHgrow(card, Priority.ALWAYS);
            card.setMaxWidth(Double.MAX_VALUE);
            studentsGrid.add(card, col, row);
        }

        Platform.runLater(this::updatePageIndicator);
    }

    private Button createStudentCard(InternationalStudentEntity student) {
        String label = student.getFirstName() + "\n" + student.getLastName().toUpperCase();

        Button card = new Button(label);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e2e4e8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-alignment: center;" +
                        "-fx-alignment: center;"
        );

        // Hover effect (même style que le skeleton de ton collègue)
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #f0f0f0;" +
                        "-fx-border-color: #c0c0c0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-alignment: center;" +
                        "-fx-alignment: center;"
        ));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e2e4e8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-alignment: center;" +
                        "-fx-alignment: center;"
        ));

        card.setOnAction(e -> handleSelectStudent(student));
        return card;
    }

    private void updatePageIndicator() {
        int totalPages = getTotalPages();
        pageIndicator.setText("Page " + (currentPage + 1) + " / " + Math.max(1, totalPages));
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) filteredStudents.size() / CARDS_PER_PAGE);
    }

    // -------------------------------------------------------------------------
    // Handlers FXML
    // -------------------------------------------------------------------------

    /**
     * Ouvre le dialog de création d'un nouvel étudiant international.
     */
    @FXML
    private void handleAddStudent() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("New international student");
        dialog.setHeaderText("Enter the new student's information");

        ButtonType saveButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("e.g. John");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("e.g. Smith");

        TextField emailField = new TextField();
        emailField.setPromptText("e.g. john.smith@email.com");

        grid.add(new Label("First name:"),      0, 0); grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last name:"),       0, 1); grid.add(lastNameField,  1, 1);
        grid.add(new Label("Personal email:"),  0, 2); grid.add(emailField,     1, 2);

        dialog.getDialogPane().setContent(grid);

        final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btSave.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!validateNotEmpty(firstNameField.getText(), "First name") ||
                    !validateNotEmpty(lastNameField.getText(), "Last name") ||
                    !validateNotEmpty(emailField.getText(), "Personal email")) {
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    studentService.create(
                            firstNameField.getText().trim(),
                            lastNameField.getText().trim(),
                            emailField.getText().trim()
                    );
                    loadStudents();
                } catch (Exception e) {
                    System.err.println("Create error: " + e.getMessage());
                    showErrorAlert("Database error", "Unable to create this student.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


    /**
     * Ouvre le dialog de suppression d'un étudiant (avec confirmation).
     * Appelé depuis le bouton "X" de chaque carte — voir handleSelectStudent
     * si tu veux plutôt ouvrir une vue détail/édition au clic sur la carte.
     */
    private void handleDeleteStudent(InternationalStudentEntity student) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete student");
        confirm.setHeaderText("Are you sure you want to delete "
                + student.getFirstName() + " " + student.getLastName() + "?");
        confirm.setContentText("This action is irreversible.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    studentService.delete(student.getEmailUniv());
                    loadStudents();
                } catch (Exception e) {
                    System.err.println("Delete error: " + e.getMessage());
                    showErrorAlert("Database error", "Unable to delete this student.");
                }
            }
        });
    }

    /**
     * Clic sur une carte étudiant → ouvre un dialog de détail / édition.
     */
    private void handleSelectStudent(InternationalStudentEntity student) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Student details");
        dialog.setHeaderText(student.getFirstName() + " " + student.getLastName().toUpperCase());

        ButtonType planningButtonType = new ButtonType("Manage planning", ButtonBar.ButtonData.LEFT);
        ButtonType deleteButtonType   = new ButtonType("Delete",          ButtonBar.ButtonData.LEFT);
        ButtonType closeButtonType    = new ButtonType("Close",           ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(
                planningButtonType, deleteButtonType, closeButtonType
        );

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.getChildren().addAll(
                new Label("University email : " + student.getEmailUniv()),
                new Label("Personal email   : " + student.getEmailPersonal())
        );
        dialog.getDialogPane().setContent(content);

        // ← Plus de dialog.close() ici, showAndWait() retourne le bouton cliqué
        dialog.showAndWait().ifPresent(btn -> {
            if (btn == deleteButtonType) {
                handleDeleteStudent(student);
            } else if (btn == planningButtonType) {
                navigateToPlanningSetup(student);
            }
        });
    }
    /**
     * Ouvre la page de sélection UFR / Langues pour cet étudiant.
     */
    private void navigateToPlanningSetup(InternationalStudentEntity student) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/staffDri-ufr-language-selection.fxml")
            );
            Parent root = loader.load();

            InternationalStudentUFRLanguageController controller = loader.getController();
            controller.setStudent(student);

            Stage stage = (Stage) studentsGrid.getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace(); // ← ajoute ça temporairement pour voir l'erreur exacte
            showErrorAlert("Error", "Unable to open the planning management page.");
        }
    }

    @FXML
    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            displayPage();
        }
    }

    @FXML
    private void handleNextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
            displayPage();
        }
    }

    // -------------------------------------------------------------------------
    // Utilitaires de validation (identiques au controller de ton collègue)
    // -------------------------------------------------------------------------

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateNotEmpty(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            showErrorAlert("Required field", "The field '" + fieldName + "' cannot be empty.");
            return false;
        }
        return true;
    }


}