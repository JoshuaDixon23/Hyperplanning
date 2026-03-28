package fr.univtln.projet.planning.controller;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import fr.univtln.projet.planning.entity.academic.Group;
import fr.univtln.projet.planning.entity.academic.GroupType;
import fr.univtln.projet.planning.entity.infrastructure.Room;
import fr.univtln.projet.planning.entity.person.Professor;
import fr.univtln.projet.planning.entity.planning.Course;
import fr.univtln.projet.planning.entity.planning.CourseType;
import fr.univtln.projet.planning.entity.planning.Module;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ModuleController implements Initializable {

    @FXML private HBox cardsContainer;
    @FXML private TextField searchField;

    // formater en heure local
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());

    private final Map<Module, List<Course>> mockDatabase = new HashMap<>();

    private final Map<Course, Group> courseGroupMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadModulesAndCoursesFromService();
    }

    private void loadModulesAndCoursesFromService() {
        cardsContainer.getChildren().clear();

        // Appel au service ...

        // En attendant
        Module moduleInfo = Module.builder()
                .code("INFO101")
                .name("Informatique")
                .ECTS(6.0f)
                .responsible(Professor.ProfessorFactory("Alan", "Turing"))
                .build();

        mockDatabase.put(moduleInfo, new ArrayList<>());

        refreshView();
    }

    private void refreshView() {
        cardsContainer.getChildren().clear();
        for (Map.Entry<Module, List<Course>> entry : mockDatabase.entrySet()) {
            createModuleCard(entry.getKey(), entry.getValue());
        }
    }

    // =========================================================================
    // UTILITAIRES DE VALIDATION ET D'AFFICHAGE D'ERREURS
    // =========================================================================

    /**
     * Affiche une boîte de dialogue d'erreur simple.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Erreur de saisie");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Valide qu'un champ texte n'est pas vide.
     */
    private boolean validateNotEmpty(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            showErrorAlert("Champ obligatoire", "Le champ '" + fieldName + "' ne peut pas être vide.");
            return false;
        }
        return true;
    }

    /**
     * Valide qu'un texte peut être converti en un nombre décimal (Float).
     */
    private boolean validateFloat(String text, String fieldName) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (NumberFormatException e) {
            showErrorAlert("Format invalide", "Le champ '" + fieldName + "' doit être un nombre valide (ex: 6.0).");
            return false;
        }
    }

    /**
     * Valide qu'un texte peut être converti en un entier positif (Long).
     */
    private boolean validatePositiveLong(String text, String fieldName) {
        try {
            long value = Long.parseLong(text);
            if (value <= 0) {
                showErrorAlert("Valeur invalide", "Le champ '" + fieldName + "' doit être supérieur à zéro.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showErrorAlert("Format invalide", "Le champ '" + fieldName + "' doit être un nombre entier valide.");
            return false;
        }
    }

    /**
     * Valide qu'un texte respecte le format d'heure (HH:mm).
     */
    private boolean validateTimeFormat(String text, String fieldName) {
        try {
            LocalTime.parse(text);
            return true;
        } catch (DateTimeParseException e) {
            showErrorAlert("Format d'heure invalide", "Le champ '" + fieldName + "' doit être au format HH:mm (ex: 08:30).");
            return false;
        }
    }

    /**
     * Valide qu'un objet (ex: issu d'une ComboBox) n'est pas null.
     */
    private boolean validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            showErrorAlert("Sélection obligatoire", "Veuillez sélectionner une option pour : " + fieldName + ".");
            return false;
        }
        return true;
    }


    /**
     * Construit visuellement une colonne pour un Module
     * @param module Le module concerné
     * @param courses La liste des cours liés à ce module (récupérée via votre service)
     */
    private void createModuleCard(Module module, List<Course> courses) {
        // hash du nom pour trouver la couleur
        int hash = module.getName().hashCode();
        String headerColor = String.format("#%06X", (0xFFFFFF & hash));

        VBox card = new VBox();
        card.setPrefWidth(280);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #d1d1d1; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        HBox header = new HBox();
        header.setPrefHeight(40);
        header.setAlignment(Pos.TOP_RIGHT);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: " + headerColor + "; -fx-background-radius: 9 9 0 0;");
        
        Button deleteModuleBtn = new Button("X"); 
        deleteModuleBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        deleteModuleBtn.setOnAction(e -> handleDeleteModule(module));
        header.getChildren().add(deleteModuleBtn);

        VBox titleContainer = new VBox(5);
        titleContainer.setPadding(new Insets(15, 15, 5, 15));
        
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(module.getName());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button editTitleBtn = new Button("✎"); 
        editTitleBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        editTitleBtn.setOnAction(e -> handleEditModule(module));
        titleBox.getChildren().addAll(titleLabel, spacer, editTitleBtn);

        String nomResponsable = (module.responsible() != null) ? module.responsible().getName() : "Non assigné";
        Label teacherLabel = new Label("Resp: " + nomResponsable);
        teacherLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666; -fx-font-style: italic;");
        
        titleContainer.getChildren().addAll(titleBox, teacherLabel);

        VBox coursesListContainer = new VBox(10);
        coursesListContainer.setPadding(new Insets(15, 15, 15, 15));
        for (Course c : courses) {
            coursesListContainer.getChildren().add(createCourseItem(module, c));
        }

        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(0, 0, 20, 0));
        Button addCourseBtn = new Button("+ add cours");
        addCourseBtn.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 5 20; -fx-cursor: hand;");
        addCourseBtn.setOnAction(e -> handleAddCourse(module));
        bottomBox.getChildren().add(addCourseBtn);

        card.getChildren().addAll(header, titleContainer, coursesListContainer, bottomBox);
        cardsContainer.getChildren().add(card);
    }

    /**
     * Construit visuellement un bloc "Cours" à partir de votre entité Course
     */
    private HBox createCourseItem(Module parentModule, Course course) {
        HBox item = new HBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: #d6eaf8; -fx-background-radius: 8;"); 

        // Calcul des heures de début
        String startTime = timeFormatter.format(course.getStartTime());
        String endTime = timeFormatter.format(course.getStartTime().plus(course.getDuration()));
        
        // Extraction Prof et Salle
        String profName = (course.getProfessors() != null && !course.getProfessors().isEmpty()) ? course.getProfessors().get(0).getName() : "Pas de prof";
        
        String roomName = "Pas de salle";
        if(course.getRoom() != null) {
            try {
                roomName = course.getRoom().getName();
            } catch (NullPointerException e) {
                roomName = "Salle " + course.getRoom().getNum(); 
            }
        }

        Group assignedGroup = courseGroupMap.get(course);
        String groupName = (assignedGroup != null) ? "Groupe " + assignedGroup.getNum() : "Pas de groupe"; 

        String infoText = String.format("%s - %s | %s\n%s | %s", 
            startTime, endTime, groupName, profName, roomName);
        
        Label infoLabel = new Label(infoText);
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("✎");
        editBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 2;");
        
        Button deleteBtn = new Button("X");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 2;");
        
        deleteBtn.setOnAction(e -> handleDeleteCourse(parentModule, course));
        editBtn.setOnAction(e -> handleEditCourse(course));

        item.getChildren().addAll(infoLabel, spacer, editBtn, deleteBtn);
        return item;
    }

    @FXML
    private void handleAddModule() {
        Dialog<Module> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Module");
        dialog.setHeaderText("Veuillez saisir les informations du nouveau module.");

        ButtonType saveButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(); nameField.setPromptText("Ex: Mathématiques");
        TextField codeField = new TextField(); codeField.setPromptText("Ex: MATH101");
        TextField ectsField = new TextField(); ectsField.setPromptText("Ex: 6.0");
        
        ComboBox<Professor> profComboBox = new ComboBox<>();
        profComboBox.getItems().addAll(getAvailableProfessors());
        profComboBox.setPromptText("Sélectionnez un responsable");
        profComboBox.setConverter(new javafx.util.StringConverter<Professor>() {
            @Override
            public String toString(Professor p) {
                return p != null ? p.getName() : "";
            }
            @Override
            public Professor fromString(String string) {
                return null; 
            }
        });

        grid.add(new Label("Nom du module:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Code:"), 0, 1); grid.add(codeField, 1, 1);
        grid.add(new Label("ECTS:"), 0, 2); grid.add(ectsField, 1, 2);
        grid.add(new Label("Responsable:"), 0, 3); grid.add(profComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btSave.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateNotEmpty(nameField.getText(), "Nom du module") ||
                !validateNotEmpty(codeField.getText(), "Code") ||
                !validateNotEmpty(ectsField.getText(), "ECTS") ||
                !validateFloat(ectsField.getText(), "ECTS") ||
                !validateNotNull(profComboBox.getValue(), "Responsable")) { // Ajout de la vérification ComboBox
                
                event.consume(); 
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    return Module.builder()
                            .name(nameField.getText())
                            .code(codeField.getText())
                            .ECTS(Float.parseFloat(ectsField.getText()))
                            .responsible(profComboBox.getValue())
                            .build();
                } catch (NumberFormatException e) {
                    System.out.println("Erreur de saisie : " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newModule -> {
            // a changer avec la vrai db
            mockDatabase.put(newModule, new ArrayList<>());
            refreshView(); 
        });
    }

    private static class CourseCreationResult {
        Course course;
        Group group;
        CourseCreationResult(Course c, Group g) { this.course = c; this.group = g; }
    }

    private void handleAddCourse(Module module) {
        Dialog<CourseCreationResult> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Cours");
        dialog.setHeaderText("Nouveau cours pour : " + module.getName());

        ButtonType saveButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField timeField = new TextField("08:00"); 
        TextField durationField = new TextField("120"); 
        
        ComboBox<CourseType> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(CourseType.values()); 

        ComboBox<Professor> profComboBox = new ComboBox<>();
        profComboBox.getItems().addAll(getAvailableProfessors());
        profComboBox.setPromptText("Sélectionnez un professeur");
        profComboBox.setConverter(new javafx.util.StringConverter<Professor>() {
            @Override public String toString(Professor p) { return p != null ? p.getName() : ""; }
            @Override public Professor fromString(String string) { return null; }
        });

        ComboBox<Room> roomComboBox = new ComboBox<>();
        roomComboBox.getItems().addAll(getAvailableRooms());
        roomComboBox.setPromptText("Sélectionnez une salle");
        roomComboBox.setConverter(new javafx.util.StringConverter<Room>() {
            @Override public String toString(Room r) { 
                if(r == null) return "";
                try { return r.getName(); } catch(NullPointerException e) { return "Salle " + r.getNum(); }
            }
            @Override public Room fromString(String string) { return null; }
        });

        ComboBox<Group> groupComboBox = new ComboBox<>();
        groupComboBox.getItems().addAll(getAvailableGroups());
        groupComboBox.setPromptText("Sélectionnez un groupe");
        groupComboBox.setConverter(new javafx.util.StringConverter<Group>() {
            @Override public String toString(Group g) { return g != null ? "Groupe " + g.getNum() + " (" + g.getType() + ")" : ""; }
            @Override public Group fromString(String string) { return null; }
        });

        grid.add(new Label("Heure de début (HH:mm):"), 0, 0); grid.add(timeField, 1, 0);
        grid.add(new Label("Durée (minutes):"), 0, 1); grid.add(durationField, 1, 1);
        grid.add(new Label("Type:"), 0, 2); grid.add(typeComboBox, 1, 2);
        grid.add(new Label("Professeur:"), 0, 3); grid.add(profComboBox, 1, 3);
        grid.add(new Label("Salle:"), 0, 4); grid.add(roomComboBox, 1, 4);
        grid.add(new Label("Groupe:"), 0, 5); grid.add(groupComboBox, 1, 5); // Ajout du groupe

        dialog.getDialogPane().setContent(grid);

        // Verification que rien n'est laissé vide
        final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btSave.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateNotEmpty(timeField.getText(), "Heure de début") ||
                !validateTimeFormat(timeField.getText(), "Heure de début") ||
                !validateNotEmpty(durationField.getText(), "Durée") ||
                !validatePositiveLong(durationField.getText(), "Durée") ||
                !validateNotNull(typeComboBox.getValue(), "Type") ||          
                !validateNotNull(profComboBox.getValue(), "Professeur") ||
                !validateNotNull(roomComboBox.getValue(), "Salle") ||          
                !validateNotNull(groupComboBox.getValue(), "Groupe")) {        
                
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    LocalTime localTime = LocalTime.parse(timeField.getText());
                    java.time.Instant startInstant = LocalDate.now().atTime(localTime).atZone(ZoneId.systemDefault()).toInstant();

                    Course newCourse = Course.builder()
                            .module(module)
                            .startTime(startInstant)
                            .duration(Duration.ofMinutes(Long.parseLong(durationField.getText())))
                            .courseType(typeComboBox.getValue())
                            .professor(profComboBox.getValue())
                            .room(roomComboBox.getValue()) 
                            .build();
                            
                    return new CourseCreationResult(newCourse, groupComboBox.getValue());
                } catch (NumberFormatException e) {
                    System.out.println("Erreur de saisie : " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            // Ajout du cours au module
            mockDatabase.get(module).add(result.course);
            
            // Assignation du groupe à ce cours
            if(result.group != null) {
                result.group.addCourse(result.course);
                courseGroupMap.put(result.course, result.group); // Pour l'affichage
            }
            
            refreshView(); 
        });
    }


    private void handleEditCourse(Course course) {
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Cours");
        dialog.setHeaderText("Modification du cours de type : " + course.getCourseType());

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField timeField = new TextField(timeFormatter.format(course.getStartTime())); 
        TextField durationField = new TextField(String.valueOf(course.getDuration().toMinutes())); 
        
        ComboBox<CourseType> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(CourseType.values()); 
        typeComboBox.getSelectionModel().select(course.getCourseType());

        ComboBox<Professor> profComboBox = new ComboBox<>();
        profComboBox.getItems().addAll(getAvailableProfessors());
        profComboBox.setConverter(new javafx.util.StringConverter<Professor>() {
            @Override public String toString(Professor p) { return p != null ? p.getName() : ""; }
            @Override public Professor fromString(String string) { return null; }
        });
        if (course.getProfessors() != null && !course.getProfessors().isEmpty()) {
            profComboBox.getSelectionModel().select(course.getProfessors().get(0));
        }

        ComboBox<Room> roomComboBox = new ComboBox<>();
        roomComboBox.getItems().addAll(getAvailableRooms());
        roomComboBox.setPromptText("Sélectionnez une salle");
        roomComboBox.setConverter(new javafx.util.StringConverter<Room>() {
            @Override public String toString(Room r) { 
                if(r == null) return "";
                try { return r.getName(); } catch(NullPointerException e) { return "Salle " + r.getNum(); }
            }
            @Override public Room fromString(String string) { return null; }
        });
        roomComboBox.getSelectionModel().select(course.getRoom());

        ComboBox<Group> groupComboBox = new ComboBox<>();
        groupComboBox.getItems().addAll(getAvailableGroups());
        groupComboBox.setConverter(new javafx.util.StringConverter<Group>() {
            @Override public String toString(Group g) { return g != null ? "Groupe " + g.getNum() + " (" + g.getType() + ")" : ""; }
            @Override public Group fromString(String string) { return null; }
        });
        // On sélectionne le groupe actuellement assigné à ce cours
        groupComboBox.getSelectionModel().select(courseGroupMap.get(course));

        grid.add(new Label("Heure de début (HH:mm):"), 0, 0); grid.add(timeField, 1, 0);
        grid.add(new Label("Durée (minutes):"), 0, 1); grid.add(durationField, 1, 1);
        grid.add(new Label("Type:"), 0, 2); grid.add(typeComboBox, 1, 2);
        grid.add(new Label("Professeur:"), 0, 3); grid.add(profComboBox, 1, 3);
        grid.add(new Label("Salle:"), 0, 4); grid.add(roomComboBox, 1, 4);
        grid.add(new Label("Groupe:"), 0, 5); grid.add(groupComboBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        
        // Verification que rien n'est laissé vide
        final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btSave.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateNotEmpty(timeField.getText(), "Heure de début") ||
                !validateTimeFormat(timeField.getText(), "Heure de début") ||
                !validateNotEmpty(durationField.getText(), "Durée") ||
                !validatePositiveLong(durationField.getText(), "Durée") ||
                !validateNotNull(typeComboBox.getValue(), "Type") ||           
                !validateNotNull(profComboBox.getValue(), "Professeur") ||     
                !validateNotNull(roomComboBox.getValue(), "Salle") ||          
                !validateNotNull(groupComboBox.getValue(), "Groupe")) {     
                
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    LocalTime localTime = LocalTime.parse(timeField.getText());
                    java.time.Instant startInstant = LocalDate.now().atTime(localTime).atZone(ZoneId.systemDefault()).toInstant();

                    course.setStartTime(startInstant);
                    course.setDuration(Duration.ofMinutes(Long.parseLong(durationField.getText())));
                    course.setCourseType(typeComboBox.getValue());
                    
                    List<Professor> updatedProfs = new ArrayList<>();
                    if (profComboBox.getValue() != null) {
                        updatedProfs.add(profComboBox.getValue());
                    }
                    course.setProfessors(updatedProfs);
                    course.setRoom(roomComboBox.getValue());
                    
                    // Mise à jour de l'assignation du groupe
                    Group oldGroup = courseGroupMap.get(course);
                    if(oldGroup != null) oldGroup.removeCourse(course);
                    
                    Group newGroup = groupComboBox.getValue();
                    if(newGroup != null) {
                        newGroup.addCourse(course);
                        courseGroupMap.put(course, newGroup);
                    } else {
                        courseGroupMap.remove(course);
                    }

                    return course;
                } catch (NumberFormatException e) {
                    System.out.println("Erreur de modification : " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedCourse -> {
            refreshView(); 
        });
    }

    private void handleEditModule(Module oldModule) {
        Dialog<Module> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Module");
        dialog.setHeaderText("Modification du module : " + oldModule.getName());

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        // Pré-remplissage avec les informations actuelles du module
        TextField nameField = new TextField(oldModule.getName()); 
        TextField codeField = new TextField(oldModule.getCode()); 
        TextField ectsField = new TextField(String.valueOf(oldModule.getECTS())); 
        
        ComboBox<Professor> profComboBox = new ComboBox<>();
        profComboBox.getItems().addAll(getAvailableProfessors());
        profComboBox.setConverter(new javafx.util.StringConverter<Professor>() {
            @Override public String toString(Professor p) { return p != null ? p.getName() : ""; }
            @Override public Professor fromString(String string) { return null; }
        });
        
        // On pré-sélectionne le responsable actuel
        if (oldModule.responsible() != null) {
            profComboBox.getSelectionModel().select(oldModule.responsible());
        }

        grid.add(new Label("Nom du module:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Code:"), 0, 1); grid.add(codeField, 1, 1);
        grid.add(new Label("ECTS:"), 0, 2); grid.add(ectsField, 1, 2);
        grid.add(new Label("Responsable:"), 0, 3); grid.add(profComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Verification que rien n'est laissé vide
        final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btSave.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateNotEmpty(nameField.getText(), "Nom du module") ||
                !validateNotEmpty(codeField.getText(), "Code") ||
                !validateNotEmpty(ectsField.getText(), "ECTS") ||
                !validateFloat(ectsField.getText(), "ECTS") ||
                !validateNotNull(profComboBox.getValue(), "Responsable")) { // Ajout ComboBox
                
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return Module.builder()
                        .name(nameField.getText())
                        .code(codeField.getText())
                        .ECTS(Float.parseFloat(ectsField.getText()))
                        .responsible(profComboBox.getValue())
                        .build();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newModule -> {
            List<Course> existingCourses = mockDatabase.remove(oldModule);
            
            mockDatabase.put(newModule, existingCourses != null ? existingCourses : new ArrayList<>());
            
            refreshView(); 
        });
    }


    private List<Professor> getAvailableProfessors() {
        return List.of(
            Professor.ProfessorFactory("Alan", "Turing"),
            Professor.ProfessorFactory("Ada", "Lovelace"),
            Professor.ProfessorFactory("Marie", "Curie"),
            Professor.ProfessorFactory("Albert", "Einstein")
        );
    }

    private List<Room> getAvailableRooms() {
        fr.univtln.projet.planning.entity.infrastructure.Building batimentTest = null; 
        fr.univtln.projet.planning.entity.infrastructure.RoomType typeTest = null; 

        return List.of(
            Room.RoomFactory(101, 50, typeTest, batimentTest),
            Room.RoomFactory(204, 30, typeTest, batimentTest),
            Room.RoomFactory(1, 200, typeTest, batimentTest) 
        );
    }

    private List<Group> getAvailableGroups() {
        return List.of(
            Group.GroupFactory(1, null),
            Group.GroupFactory(2, null),
            Group.GroupFactory(3, null)
        );
    }

    private void handleDeleteModule(Module module) {
        System.out.println("Supprimer le module : " + module.getName());
        
        List<Course> courses = mockDatabase.get(module);
        if(courses != null) {
            for(Course c : courses) {
                Group g = courseGroupMap.get(c);
                if(g != null) g.removeCourse(c);
                courseGroupMap.remove(c);
            }
        }
        
        mockDatabase.remove(module);
        refreshView();
    }

    private void handleDeleteCourse(Module parentModule, Course course) {
        System.out.println("Supprimer un cours");
        
        Group g = courseGroupMap.get(course);
        if(g != null) g.removeCourse(course);
        courseGroupMap.remove(course);

        mockDatabase.get(parentModule).remove(course);
        refreshView();
    }
}