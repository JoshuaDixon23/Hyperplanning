package fr.univtln.projet.planning.controller;

import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.infrastructure.RoomEntity;
import fr.univtln.projet.planning.entity.person.ProfessorEntity;
import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.modele.planning.CourseType;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import fr.univtln.projet.planning.service.infrastructureService.RoomService;
import fr.univtln.projet.planning.service.planningService.CourseService;
import fr.univtln.projet.planning.service.planningService.ModuleService;
import fr.univtln.projet.planning.service.personService.ProfessorService;
import fr.univtln.projet.planning.service.academicService.GroupService;
import fr.univtln.projet.planning.service.ServiceRegistry;

public class ModuleController implements Initializable {

    @FXML private HBox cardsContainer;
    @FXML private TextField searchField;
    @FXML private Label pageIndicator;
    
    private RoomService roomService;
    private ModuleService moduleService;
    private ProfessorService professorService;
    private GroupService groupService;
    private CourseService courseService; 
    
    // Cache et Pagination
    private List<ModuleEntity> allModulesCache = new ArrayList<>(); 
    private final int pageSize = 3;
    private int pageNumber = 0; 
    
    private LocalDate startOfAcademicYear;
    private LocalDate endOfAcademicYear;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());

    private final ObservableList<ProfessorEntity> cachedProfessors = FXCollections.observableArrayList();
    private final ObservableList<RoomEntity> cachedRooms = FXCollections.observableArrayList();
    private final ObservableList<GroupEntity> cachedGroups = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            roomService = ServiceRegistry.getRoomService();
            moduleService = ServiceRegistry.getModuleService();
            professorService = ServiceRegistry.getProfessorService();
            groupService = ServiceRegistry.getGroupService();
            courseService = ServiceRegistry.getCourseService();

            LocalDate today = LocalDate.now();
            int currentYear = today.getYear();

            int startYear = (today.getMonthValue() >= 9) ? currentYear : currentYear - 1;

            startOfAcademicYear = LocalDate.of(startYear, 9, 1);
            endOfAcademicYear = LocalDate.of(startYear + 1, 7, 1);

        } catch (IllegalStateException e) {
            System.err.println("ServiceRegistry not initialized: " + e.getMessage());
            return;
        }
        
        preloadReferenceData();
        loadModulesAndCoursesFromService();
    }

    private void preloadReferenceData() {
        try {
            cachedProfessors.setAll(professorService.findAll());
            cachedRooms.setAll(roomService.findAll());
            cachedGroups.setAll(groupService.findAll());
        } catch (Exception e) {
            System.err.println("Erreur lors du préchargement des données : " + e.getMessage());
        }
    }

    // =========================================================================
    // CHARGEMENT ET PAGINATION
    // =========================================================================

    private void loadModulesAndCoursesFromService() {
        try {
            allModulesCache = moduleService.findAll();
            updateUI();
        } catch (Exception e) {
            System.err.println("Error loading modules: " + e.getMessage());
        }
    }

    @FXML
    public void handlePreviousPage() {
        if (pageNumber > 0) {
            pageNumber--;
            updateUI();
        }
    }

    @FXML
    public void handleNextPage() {
        if ((pageNumber + 1) * pageSize < allModulesCache.size()) {
            pageNumber++;
            updateUI();
        }
    }

    private void updateUI() {
        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, allModulesCache.size());
        if (start >= allModulesCache.size() && pageNumber > 0) {
            System.out.println("Out of pages ");
        }else{
            cardsContainer.getChildren().clear();
            
            List<ModuleEntity> pageItems = allModulesCache.subList(start, end);
            if (pageIndicator != null) {
                pageIndicator.setText("Chargement...");
            }

            // 1. Création des squelettes d'interface sur le Thread UI
            Map<ModuleEntity, VBox> cardMap = new HashMap<>();
            for (ModuleEntity module : pageItems) {
                VBox card = createModuleCardSkeleton(module);
                cardsContainer.getChildren().add(card);
                cardMap.put(module, card);
            }

            // 2. Lancement du chargement des cours en arrière-plan
            Thread loaderThread = new Thread(() -> {
                for (ModuleEntity module : pageItems) {
                    List<CourseEntity> courses = courseService.findPlanningByModule(module, startOfAcademicYear, endOfAcademicYear);

                    // 3. Mise à jour de la carte spécifique sur le Thread UI
                    Platform.runLater(() -> {
                        fillCardWithCourses(cardMap.get(module), courses);
                        
                        if (module.equals(pageItems.get(pageItems.size() - 1)) && pageIndicator != null) {
                            pageIndicator.setText("Page " + (pageNumber + 1));
                        }
                    });
                }
            });

            loaderThread.setDaemon(true);
            loaderThread.start();
        }
    }

    // =========================================================================
    // CREATION DE L'UI (SQUELETTE ET REMPLISSAGE)
    // =========================================================================

    private VBox createModuleCardSkeleton(ModuleEntity module) {
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

        String nomResponsable = (module.responsible() != null) ? module.responsible().toString(): "Non assigné";
        Label teacherLabel = new Label("Resp: " + nomResponsable);
        teacherLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666; -fx-font-style: italic;");
        titleContainer.getChildren().addAll(titleBox, teacherLabel);

        // Conteneur vide pour les cours, avec un identifiant pour le retrouver
        VBox coursesListContainer = new VBox(10);
        coursesListContainer.setId("coursesContainer");
        coursesListContainer.setPadding(new Insets(15, 15, 15, 15));
        
        Label loadingLabel = new Label("Chargement des cours...");
        loadingLabel.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
        coursesListContainer.getChildren().add(loadingLabel);

        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(0, 0, 20, 0));
        Button addCourseBtn = new Button("+ add cours");
        addCourseBtn.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 5 20; -fx-cursor: hand;");
        addCourseBtn.setOnAction(e -> handleAddCourse(module));
        bottomBox.getChildren().add(addCourseBtn);

        card.getChildren().addAll(header, titleContainer, coursesListContainer, bottomBox);
        return card;
    }

    private void fillCardWithCourses(VBox card, List<CourseEntity> courses) {
        VBox container = (VBox) card.lookup("#coursesContainer");
        if (container != null) {
            container.getChildren().clear();
            
            if (courses.isEmpty()) {
                Label noCourseLabel = new Label("Aucun cours programmé.");
                noCourseLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #999;");
                container.getChildren().add(noCourseLabel);
            } else {
                for (CourseEntity c : courses) {
                    container.getChildren().add(createCourseItem(c));
                }
            }
        }
    }

    private HBox createCourseItem(CourseEntity course) {
        HBox item = new HBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: #d6eaf8; -fx-background-radius: 8;"); 

        String startTime = timeFormatter.format(course.getStartTime());
        String endTime = timeFormatter.format(course.getStartTime().plus(course.getDuration()));
        
        String profName = course.getProfessors() != null && !course.getProfessors().isEmpty() 
            ? course.getProfessors().stream().findFirst().get().getName() 
            : "Pas de prof";
        
        String roomName = "Pas de salle";
        if(course.getRoom() != null) {
            try {
                roomName = course.getRoom().getName();
            } catch (NullPointerException e) {
                roomName = "Salle " + course.getRoom().getName(); 
            }
        }

        GroupEntity assignedGroup = null;
        try {
            assignedGroup = course.getGroups().stream().findFirst().orElse(null);
        } catch (Exception e) {
            System.err.println("Impossible de charger le groupe pour ce cours : " + e.getMessage());
        }

        String groupName = (assignedGroup != null) ? "Groupe " + assignedGroup.getNum() : "Pas de groupe"; 

        String infoText = String.format("%s - %s | %s\n%s | %s", startTime, endTime, groupName, profName, roomName);
        
        Label infoLabel = new Label(infoText);
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("✎");
        editBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 2;");
        
        Button deleteBtn = new Button("X");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 2;");
        
        deleteBtn.setOnAction(e -> handleDeleteCourse(course));
        editBtn.setOnAction(e -> handleEditCourse(course));

        item.getChildren().addAll(infoLabel, spacer, editBtn, deleteBtn);
        return item;
    }

    // =========================================================================
    // UTILITAIRES DE VALIDATION
    // =========================================================================

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateNotEmpty(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            showErrorAlert("Champ obligatoire", "Le champ '" + fieldName + "' ne peut pas être vide.");
            return false;
        }
        return true;
    }

    private boolean validateFloat(String text, String fieldName) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (NumberFormatException e) {
            showErrorAlert("Format invalide", "Le champ '" + fieldName + "' doit être un nombre valide (ex: 6.0).");
            return false;
        }
    }

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

    private boolean validateTimeFormat(String text, String fieldName) {
        try {
            LocalTime.parse(text);
            return true;
        } catch (DateTimeParseException e) {
            showErrorAlert("Format d'heure invalide", "Le champ '" + fieldName + "' doit être au format HH:mm (ex: 08:30).");
            return false;
        }
    }

    private boolean validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            showErrorAlert("Sélection obligatoire", "Veuillez sélectionner une option pour : " + fieldName + ".");
            return false;
        }
        return true;
    }

    // =========================================================================
    // HANDLERS D'ACTIONS
    // =========================================================================

    @FXML
    private void handleAddModule() {
        Dialog<ModuleEntity> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Module");
        dialog.setHeaderText("Veuillez saisir les informations du nouveau module.");

        ButtonType saveButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(); nameField.setPromptText("Ex: Mathématiques");
        TextField codeField = new TextField(); codeField.setPromptText("Ex: MATH101");
        TextField ectsField = new TextField(); ectsField.setPromptText("Ex: 6.0");
        
        ComboBox<ProfessorEntity> profComboBox = new ComboBox<>();
        profComboBox.setItems(cachedProfessors);
        profComboBox.setPromptText("Sélectionnez un responsable");

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
                !validateNotNull(profComboBox.getValue(), "Responsable")) { 
                event.consume(); 
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return ModuleEntity.builder()
                        .name(nameField.getText())
                        .code(codeField.getText())
                        .ECTS(Float.parseFloat(ectsField.getText()))
                        .responsible(profComboBox.getValue())
                        .build();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newModule -> {
            try {
                moduleService.create(newModule);
                loadModulesAndCoursesFromService(); 
            } catch (Exception e) {
                System.err.println("Error saving module: " + e.getMessage());
                showErrorAlert("Erreur DB", "Impossible de sauvegarder le module.");
            }
        });
    }

    private void handleAddCourse(ModuleEntity module) {
        Dialog<CourseEntity> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Cours");
        dialog.setHeaderText("Nouveau cours pour : " + module.getName());

        ButtonType saveButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker datePicker = new DatePicker(LocalDate.now());

        TextField timeField = new TextField("08:00"); 
        TextField durationField = new TextField("120"); 
        
        ComboBox<CourseType> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(CourseType.values()); 

        ComboBox<ProfessorEntity> profComboBox = new ComboBox<>();
        profComboBox.setItems(cachedProfessors);
        profComboBox.setPromptText("Sélectionnez un professeur");

        ComboBox<RoomEntity> roomComboBox = new ComboBox<>();
        roomComboBox.setItems(cachedRooms);
        roomComboBox.setPromptText("Sélectionnez une salle");

        ComboBox<GroupEntity> groupComboBox = new ComboBox<>();
        groupComboBox.setItems(cachedGroups);
        groupComboBox.setPromptText("Sélectionnez un groupe");
        groupComboBox.setConverter(new javafx.util.StringConverter<GroupEntity>() {
            @Override public String toString(GroupEntity g) { return g != null ? "Groupe " + g.getNum() + " (" + g.getType() + ")" : ""; }
            @Override public GroupEntity fromString(String string) { return null; }
        });

        grid.add(new Label("Date:"), 0, 0); 
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Heure de début (HH:mm):"), 0, 1); grid.add(timeField, 1, 1);
        grid.add(new Label("Durée (minutes):"), 0, 2); grid.add(durationField, 1, 2);
        grid.add(new Label("Type:"), 0, 3); grid.add(typeComboBox, 1, 3);
        grid.add(new Label("Professeur:"), 0, 4); grid.add(profComboBox, 1, 4);
        grid.add(new Label("Salle:"), 0, 5); grid.add(roomComboBox, 1, 5);
        grid.add(new Label("Groupe:"), 0, 6); grid.add(groupComboBox, 1, 6); 

        dialog.getDialogPane().setContent(grid);

        final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btSave.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateNotEmpty(timeField.getText(), "Heure de début") ||
                !validateTimeFormat(timeField.getText(), "Heure de début") ||
                !validateNotEmpty(durationField.getText(), "Durée") ||
                !validatePositiveLong(durationField.getText(), "Durée") ||
                !validateNotNull(typeComboBox.getValue(), "Type") ||          
                !validateNotNull(profComboBox.getValue(), "Professeur") ||
                !validateNotNull(roomComboBox.getValue(), "Salle") ||          
                !validateNotNull(groupComboBox.getValue(), "Groupe") ||
                !validateNotNull(datePicker.getValue(), "Date")) {        
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                LocalTime localTime = LocalTime.parse(timeField.getText());
                LocalDate selectedDate = datePicker.getValue();

                CourseEntity newCourse = CourseEntity.builder()
                        .module(module)
                        .date(selectedDate)
                        .startTime(localTime)
                        .duration(Duration.ofMinutes(Long.parseLong(durationField.getText())))
                        .courseType(typeComboBox.getValue())
                        .professor(profComboBox.getValue())
                        .room(roomComboBox.getValue()) 
                        .build();
                
                GroupEntity selectedGroup = groupComboBox.getValue();
                if (selectedGroup != null) {
                    newCourse.addGroup(selectedGroup); 
                }

                return newCourse;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(courseToSave -> {
            try {
                courseService.create(courseToSave); 
                
                loadModulesAndCoursesFromService(); 
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error saving course: " + e.getMessage());
                showErrorAlert("Erreur DB", "Impossible de sauvegarder le cours.");
            }
        });
    }

    private void handleEditCourse(CourseEntity course) {
        Dialog<CourseEntity> dialog = new Dialog<>();
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

        ComboBox<ProfessorEntity> profComboBox = new ComboBox<>();
        profComboBox.setItems(cachedProfessors);
        profComboBox.setConverter(new javafx.util.StringConverter<ProfessorEntity>() {
            @Override public String toString(ProfessorEntity p) { return p != null ? p.toString() : ""; }
            @Override public ProfessorEntity fromString(String string) { return null; }
        });
        if (course.getProfessors() != null && !course.getProfessors().isEmpty()) {
            profComboBox.getSelectionModel().select(
                course.getProfessors().stream().findFirst().orElse(null)
            );
        }

        ComboBox<RoomEntity> roomComboBox = new ComboBox<>();
        roomComboBox.setItems(cachedRooms);
        roomComboBox.setPromptText("Sélectionnez une salle");
        roomComboBox.setConverter(new javafx.util.StringConverter<RoomEntity>() {
            @Override public String toString(RoomEntity r) { 
                if(r == null) return "";
                try { return r.getName(); } catch(NullPointerException e) { return "Salle " + r.getName(); }
            }
            @Override public RoomEntity fromString(String string) { return null; }
        });
        roomComboBox.getSelectionModel().select(course.getRoom());

        ComboBox<GroupEntity> groupComboBox = new ComboBox<>();
        groupComboBox.setItems(cachedGroups);
        groupComboBox.setConverter(new javafx.util.StringConverter<GroupEntity>() {
            @Override public String toString(GroupEntity g) { return g != null ? "Groupe " + g.getNum() + " (" + g.getType() + ")" : ""; }
            @Override public GroupEntity fromString(String string) { return null; }
        });
        
        GroupEntity oldGroup = null;
        try {
            oldGroup = course.getGroups().stream().findFirst().orElse(null);
        } catch(Exception ignored){}
        groupComboBox.getSelectionModel().select(oldGroup);

        grid.add(new Label("Heure de début (HH:mm):"), 0, 0); grid.add(timeField, 1, 0);
        grid.add(new Label("Durée (minutes):"), 0, 1); grid.add(durationField, 1, 1);
        grid.add(new Label("Type:"), 0, 2); grid.add(typeComboBox, 1, 2);
        grid.add(new Label("Professeur:"), 0, 3); grid.add(profComboBox, 1, 3);
        grid.add(new Label("Salle:"), 0, 4); grid.add(roomComboBox, 1, 4);
        grid.add(new Label("Groupe:"), 0, 5); grid.add(groupComboBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        
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

        final GroupEntity finalOldGroup = oldGroup; 
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                LocalTime localTime = LocalTime.parse(timeField.getText());

                course.setStartTime(localTime);
                course.setDuration(Duration.ofMinutes(Long.parseLong(durationField.getText())));
                course.setCourseType(typeComboBox.getValue());
                
                HashSet<ProfessorEntity> updatedProfs = new HashSet<>();
                if (profComboBox.getValue() != null) {
                    updatedProfs.add(profComboBox.getValue());
                }
                course.setProfessors(updatedProfs);
                course.setRoom(roomComboBox.getValue());

                GroupEntity newGroup = groupComboBox.getValue();
                if (finalOldGroup != null && !finalOldGroup.equals(newGroup)) {
                    finalOldGroup.removeCourse(course);
                    groupService.create(finalOldGroup); 
                }
                if (newGroup != null && !newGroup.equals(finalOldGroup)) {
                    newGroup.addCourse(course);
                    groupService.create(newGroup); 
                }
                
                return course;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedCourse -> {
            try {
                courseService.create(updatedCourse); 
                loadModulesAndCoursesFromService(); 
            } catch (Exception e) {
                System.err.println("Error updating course: " + e.getMessage());
                showErrorAlert("Erreur DB", "Impossible de mettre à jour le cours.");
            }
        });
    }

    private void handleEditModule(ModuleEntity oldModule) {
        Dialog<ModuleEntity> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Module");
        dialog.setHeaderText("Modification du module : " + oldModule.getName());

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(oldModule.getName()); 
        TextField codeField = new TextField(oldModule.getCode()); 
        TextField ectsField = new TextField(String.valueOf(oldModule.getECTS())); 
        
        ComboBox<ProfessorEntity> profComboBox = new ComboBox<>();
        profComboBox.setItems(cachedProfessors);
        profComboBox.setConverter(new javafx.util.StringConverter<ProfessorEntity>() {
            @Override public String toString(ProfessorEntity p) { return p != null ? p.getName() : ""; }
            @Override public ProfessorEntity fromString(String string) { return null; }
        });
        
        if (oldModule.responsible() != null) {
            profComboBox.getSelectionModel().select(oldModule.responsible());
        }

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
                !validateNotNull(profComboBox.getValue(), "Responsable")) { 
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                oldModule.setName(nameField.getText());
                oldModule.setCode(codeField.getText());
                oldModule.setECTS(Float.parseFloat(ectsField.getText()));
                oldModule.setResponsible(profComboBox.getValue());
                return oldModule;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedModule -> {
            try {
                moduleService.create(updatedModule); 
                loadModulesAndCoursesFromService(); 
            } catch (Exception e) {
                System.err.println("Error updating module: " + e.getMessage());
                showErrorAlert("Erreur DB", "Impossible de mettre à jour le module.");
            }
        });
    }

    private void handleDeleteModule(ModuleEntity module) {
        try {
            moduleService.delete(module); 
            loadModulesAndCoursesFromService();
        } catch (Exception e) {
            System.err.println("Error deleting module: " + e.getMessage());
            showErrorAlert("Erreur DB", "Impossible de supprimer le module.");
        }
    }

    private void handleDeleteCourse(CourseEntity course) {
        try {
            GroupEntity g = course.getGroups().stream().findFirst().orElse(null);
            if(g != null) {
                g.removeCourse(course);
                groupService.create(g); 
            }

            courseService.delete(course.getCourseId()); 
            loadModulesAndCoursesFromService();
        } catch (Exception e) {
            System.err.println("Error deleting course: " + e.getMessage());
            showErrorAlert("Erreur DB", "Impossible de supprimer le cours.");
        }
    }
}