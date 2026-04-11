package fr.univtln.projet.planning.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.mapper.academic.GroupMapper;
import fr.univtln.projet.planning.mapper.planning.ModuleMapper;
import fr.univtln.projet.planning.modele.academic.GroupType;
import fr.univtln.projet.planning.modele.international.BasketFinal;
import fr.univtln.projet.planning.modele.person.InternationalStudent;
import fr.univtln.projet.planning.service.internationalService.BasketFinalService;
import fr.univtln.projet.planning.service.planningService.CourseService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DRIController {

    @FXML private Label studentTitleLabel;
    @FXML private Label userNameLabel;
    @FXML private Label ectsLabel;
    @FXML private Label conflictsLabel;
    @FXML private Button logoutButton;
    
    @FXML private VBox basketContainer;
    @FXML private GridPane planningGrid;
    @FXML private HBox weeksContainer;
    @FXML private StackPane weeksViewport;
    @FXML private VBox timeColumn;
    @FXML private ScrollPane planningScroll;
    @FXML private ScrollPane timeScroll;
    @FXML private Pane coursesPane;
    @FXML private StackPane planningContent;

    private static final int GRID_START_HOUR = 8;
    private static final int SLOT_MINUTES = 30;
    private static final double ROW_HEIGHT = 60;

    private int weeksShown = 6;
    private LocalDate baseWeekMonday;
    private LocalDate selectedWeekMonday;
    private final WeekFields weekFields = WeekFields.ISO;
    private final DateTimeFormatter dayMonthFmt = DateTimeFormatter.ofPattern("d MMM", Locale.FRENCH);

    private LocalDate startOfAcademicYear;
    private LocalDate endOfAcademicYear;

    private CourseService courseService;
    private BasketFinalService basketFinalService; 
    
    private InternationalStudent currentStudent;
    private List<ModuleEntity> inputModules = new ArrayList<>();
    
    private final Map<String, Map<GroupType, GroupEntity>> selectedGroupsPerModuleAndType = new HashMap<>();
    private final Map<String, Boolean> moduleSelectionStatus = new HashMap<>();
    
    private final List<CourseEntity> allAcademicYearCourses = new ArrayList<>();
    private List<CourseEntity> currentActiveCourses = new ArrayList<>();
    private Set<CourseEntity> allConflictingCourses = new HashSet<>();
    
    private final List<VBox> sourceCourseCards = new ArrayList<>();
    private final Map<String, Map<GroupType, List<RadioButton>>> moduleRadioButtons = new HashMap<>();

    @FXML
    public void initialize() {
        selectedWeekMonday = mondayOf(LocalDate.now());
        baseWeekMonday = selectedWeekMonday;

        int currentYear = LocalDate.now().getYear();
        int startYear = (LocalDate.now().getMonthValue() >= 9) ? currentYear : currentYear - 1;
        startOfAcademicYear = LocalDate.of(startYear, 9, 1);
        endOfAcademicYear = LocalDate.of(startYear + 1, 7, 31);

        Platform.runLater(this::applyWeeksClip);
        renderWeeksInto(weeksContainer, baseWeekMonday);
        buildEmptyGrid(8, 20, 1);

        coursesPane.setPickOnBounds(false);
        planningGrid.widthProperty().addListener((obs, oldVal, newVal) -> layoutCoursesStacked());

        coursesPane.addEventFilter(javafx.scene.input.ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY();
            double contentHeight = planningContent.getHeight();
            double viewportHeight = planningScroll.getViewportBounds().getHeight();
            if (contentHeight > viewportHeight) {
                double v = planningScroll.getVvalue();
                double change = -deltaY / (contentHeight - viewportHeight);
                planningScroll.setVvalue(Math.max(0, Math.min(1, v + change)));
            }
            event.consume();
        });

        weeksViewport.widthProperty().addListener((obs, oldVal, newVal) -> {
            int newCount = computeWeeksShown();
            if (newCount != weeksShown) {
                weeksShown = newCount;
                renderWeeksInto(weeksContainer, baseWeekMonday);
            }
        });

        Platform.runLater(() -> {
            timeScroll.vvalueProperty().bindBidirectional(planningScroll.vvalueProperty());
            weeksShown = computeWeeksShown();
            renderWeeksInto(weeksContainer, baseWeekMonday);
            
            coursesPane.prefWidthProperty().bind(planningGrid.widthProperty());
            coursesPane.minWidthProperty().bind(planningGrid.widthProperty());
            coursesPane.maxWidthProperty().bind(planningGrid.widthProperty());
            coursesPane.prefHeightProperty().bind(planningGrid.heightProperty());
            coursesPane.minHeightProperty().bind(planningGrid.heightProperty());
            coursesPane.maxHeightProperty().bind(planningGrid.heightProperty());
        });
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setBasketFinalService(BasketFinalService basketFinalService) {
        this.basketFinalService = basketFinalService;
    }

    public void loadStudentDRI(InternationalStudent student) {
        this.currentStudent = student;

        if (currentStudent != null && currentStudent.getBasketFinal() != null && currentStudent.getBasketFinal().getEntries() != null) {
            this.inputModules = currentStudent.getBasketFinal().getEntries().stream()
                    .map(entry -> ModuleMapper.toDomain(entry.getModule())) 
                    .collect(Collectors.toList());
        } else {
            this.inputModules = new ArrayList<>();
        }

        if (studentTitleLabel != null) {
            studentTitleLabel.setText("Planning de l'étudiant(e) : " + student.getFirstName() + " " + student.getLastName());
        }
        
        for (ModuleEntity m : inputModules) {
            moduleSelectionStatus.put(m.getCode(), true);
            selectedGroupsPerModuleAndType.put(m.getCode(), new HashMap<>());
        }

        buildBasket();
        loadAllAcademicYearCourses(); 
    }

    private void buildBasket() {
        if (basketContainer == null) return;
        basketContainer.getChildren().clear();
        moduleRadioButtons.clear();

        for (ModuleEntity module : inputModules) {
            moduleRadioButtons.put(module.getCode(), new HashMap<>());

            VBox card = new VBox();
            card.setStyle("-fx-border-color: #d1d1d1; -fx-background-color: #f9f9f9;");

            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(new Insets(10));

            CheckBox activeCheck = new CheckBox();
            activeCheck.setSelected(true);
            activeCheck.setOnAction(e -> {
                boolean selected = activeCheck.isSelected();
                moduleSelectionStatus.put(module.getCode(), selected);

                Map<GroupType, List<RadioButton>> radiosForModule = moduleRadioButtons.get(module.getCode());
                if (radiosForModule != null) {
                    for (Map.Entry<GroupType, List<RadioButton>> entry : radiosForModule.entrySet()) {
                        if (!selected) {
                            entry.getValue().forEach(rb -> {
                                rb.setSelected(false);
                                rb.setDisable(true);
                            });
                            selectedGroupsPerModuleAndType.get(module.getCode()).remove(entry.getKey());
                        } else {
                            List<RadioButton> rbs = entry.getValue();
                            rbs.forEach(rb -> rb.setDisable(false));
                            if (!rbs.isEmpty()) {
                                rbs.get(0).setSelected(true);
                                GroupEntity grp = (GroupEntity) rbs.get(0).getUserData();
                                selectedGroupsPerModuleAndType.get(module.getCode()).put(entry.getKey(), grp);
                            }
                        }
                    }
                }
                updateDRIState();
            });

            Label nameLbl = new Label(module.getName());
            nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Button infoBtn = new Button("info");

            header.getChildren().addAll(activeCheck, nameLbl, spacer, infoBtn);

            VBox body = new VBox(5);
            body.setPadding(new Insets(10));
            body.setStyle("-fx-background-color: #ececec; -fx-border-color: #d1d1d1; -fx-border-width: 1 0 0 0;");

            Label detailsLbl = new Label("Code : " + module.getCode() + " | ECTS : " + module.getECTS());
            detailsLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");
            body.getChildren().add(detailsLbl);

            Map<String, GroupEntity> uniqueGroupsMap = new HashMap<>();

            if (courseService != null) {
                List<CourseEntity> moduleCourses = courseService.findPlanningByModuleCode(
                        module.getCode(), startOfAcademicYear, endOfAcademicYear);

                if (moduleCourses != null) {
                    for (CourseEntity c : moduleCourses) {
                        if (c.getGroups() != null) {
                            for (GroupEntity g : c.getGroups()) {
                                String key = g.getType().name() + "_" + g.getNum() + "_" + g.getPromo().getName();
                                uniqueGroupsMap.put(key, g);
                            }
                        }
                    }
                }
            }

            List<GroupEntity> availableGroups = new ArrayList<>(uniqueGroupsMap.values());

            Map<GroupType, List<GroupEntity>> groupsByType = new HashMap<>();
            for (GroupEntity g : availableGroups) {
                groupsByType.computeIfAbsent(g.getType(), k -> new ArrayList<>()).add(g);
            }

            boolean hasGroups = false;

            for (GroupType type : GroupType.values()) {
                List<GroupEntity> groupsOfThisType = groupsByType.get(type);

                if (groupsOfThisType != null && !groupsOfThisType.isEmpty()) {
                    groupsOfThisType.sort(java.util.Comparator.comparingInt(GroupEntity::getNum));

                    if (type == GroupType.PROMO) {
                        selectedGroupsPerModuleAndType.get(module.getCode()).put(GroupType.PROMO, groupsOfThisType.get(0));
                        Label cmLabel = new Label("📌 Inclus : Cours Magistraux (CM)");
                        cmLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #666; -fx-padding: 5 0 0 0;");
                        body.getChildren().add(cmLabel);
                    } else {
                        createGroupSectionUI(body, module, groupsOfThisType, getSectionTitle(type), type);
                    }
                    hasGroups = true;
                }
            }

            if (!hasGroups) {
                body.getChildren().add(new Label("Aucun groupe spécifique trouvé."));
            }

            body.setVisible(false);
            body.setManaged(false);
            infoBtn.setOnAction(e -> {
                boolean isVisible = body.isVisible();
                body.setVisible(!isVisible);
                body.setManaged(!isVisible);
            });

            card.getChildren().addAll(header, body);
            basketContainer.getChildren().add(card);
        }

        Button validateBtn = new Button("Valider le planning");
        validateBtn.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");
        validateBtn.setMaxWidth(Double.MAX_VALUE);
        validateBtn.setOnAction(e -> handleValidateBasket());
        VBox.setMargin(validateBtn, new Insets(15, 0, 0, 0));

        basketContainer.getChildren().add(validateBtn);
    }

    @FXML
    private void handleValidateBasket() {
        if (currentStudent == null) {
            System.err.println("Erreur: Aucun étudiant sélectionné.");
            return;
        }
        if (basketFinalService == null) {
            System.err.println("Erreur: Le BasketFinalService n'a pas été défini.");
            return;
        }

        try {
            // 1. On crée le panier final pour cet étudiant via le service
            BasketFinal basket = basketFinalService.create();
            basket.setStudent(currentStudent);

            // 2. On parcourt les choix effectués par l'agent DRI dans l'interface
            for (ModuleEntity module : inputModules) {
                if (moduleSelectionStatus.getOrDefault(module.getCode(), false)) {
                    Map<GroupType, GroupEntity> groups = selectedGroupsPerModuleAndType.get(module.getCode());
                    
                    if (groups != null) {
                        for (GroupEntity group : groups.values()) {
                            // Utilisation des Mappers pour repasser de Entity à Modèle pour l'enregistrement
                            basketFinalService.addModule(
                                basket.getId(), 
                                ModuleMapper.toJpa(module), 
                                GroupMapper.toJpa(group)
                            );
                        }
                    }
                }
            }

            System.out.println("✅ VALIDATION DU PLANNING RÉUSSIE pour " + currentStudent.getFirstName());
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation Réussie");
            alert.setHeaderText("Panier Final Enregistré !");
            alert.setContentText("Le planning a été validé avec succès pour l'étudiant " 
                                + currentStudent.getFirstName() + ".\n"
                                + "Conflits restants : " + allConflictingCourses.size());
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur de sauvegarde");
            error.setHeaderText(null);
            error.setContentText("Impossible d'enregistrer le BasketFinal dans la base de données.");
            error.showAndWait();
        }
    }

    private String getSectionTitle(GroupType type) {
        return switch (type) {
            case PROMO -> "Cours Magistraux (CM)";
            case TD -> "Travaux Dirigés (TD)";
            case TP -> "Travaux Pratiques (TP)";
            default -> type.name();
        };
    }


    private void createGroupSectionUI(VBox parent, ModuleEntity module, List<GroupEntity> groups, String title, GroupType type) {
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 2 0; -fx-text-fill: #333;");
        parent.getChildren().add(lblTitle);

        ToggleGroup tg = new ToggleGroup();
        List<RadioButton> radioButtons = new ArrayList<>();
        boolean first = true;

        for (GroupEntity grp : groups) {
            RadioButton rb = new RadioButton(grp.getPromo().getName() + " " + grp.getPromo().getStudyLevel() + " - Groupe " + grp.getNum());
            rb.setToggleGroup(tg);
            rb.setUserData(grp);

            if (first) {
                rb.setSelected(true);
                selectedGroupsPerModuleAndType.get(module.getCode()).put(type, grp);
                first = false;
            }

            rb.setOnAction(e -> {
                if (rb.isSelected()) {
                    selectedGroupsPerModuleAndType.get(module.getCode()).put(type, grp);
                    updateDRIState();
                }
            });

            parent.getChildren().add(rb);
            radioButtons.add(rb);
        }

        moduleRadioButtons
                .computeIfAbsent(module.getCode(), k -> new HashMap<>())
                .put(type, radioButtons);
    }

    private void updateDRIState() {
        float totalEcts = 0;
        for (ModuleEntity m : inputModules) {
            if (moduleSelectionStatus.getOrDefault(m.getCode(), false)) {
                totalEcts += m.getECTS();
            }
        }
        if (ectsLabel != null) {
            ectsLabel.setText(String.valueOf(totalEcts));
        }

        currentActiveCourses = getActiveCourses(allAcademicYearCourses);
        allConflictingCourses = findOverlaps(currentActiveCourses);
        
        if (conflictsLabel != null) {
            conflictsLabel.setText(String.valueOf(allConflictingCourses.size()));
        }

        renderWeeksInto(weeksContainer, baseWeekMonday);
        buildPlanningCards();
    }

    private void loadAllAcademicYearCourses() {
        if (courseService == null) return;
        allAcademicYearCourses.clear();

        for (ModuleEntity module : inputModules) {
            List<CourseEntity> courses = courseService.findPlanningByModuleCode(module.getCode(), startOfAcademicYear, endOfAcademicYear);
            if (courses != null) {
                allAcademicYearCourses.addAll(courses);
            }
        }
        updateDRIState();
    }

    private List<CourseEntity> getActiveCourses(List<CourseEntity> rawCourses) {
        List<CourseEntity> active = new ArrayList<>();
        for (CourseEntity course : rawCourses) {
            ModuleEntity module = course.getModule();
            if (module == null || !moduleSelectionStatus.getOrDefault(module.getCode(), false)) continue;

            Map<GroupType, GroupEntity> studentChoicesForModule = selectedGroupsPerModuleAndType.get(module.getCode());
            boolean keepCourse = false;
            
            if (course.getGroups() == null || course.getGroups().isEmpty()) {
                keepCourse = true; 
            } else {
                for (GroupEntity courseGroup : course.getGroups()) {
                    GroupEntity studentChoice = studentChoicesForModule != null ? studentChoicesForModule.get(courseGroup.getType()) : null;
                    
                    if (studentChoice != null 
                            && courseGroup.getNum() == studentChoice.getNum()
                            && Objects.equals(courseGroup.getType(), studentChoice.getType())
                            && Objects.equals(courseGroup.getPromo().getName(), studentChoice.getPromo().getName())) {
                        
                        keepCourse = true;
                        break;
                    }
                }
            }

            if (keepCourse) {
                active.add(course);
            }
        }
        return active;
    }

    private Set<CourseEntity> findOverlaps(List<CourseEntity> courses) {
        Set<CourseEntity> conflicts = new HashSet<>();
        for (int i = 0; i < courses.size(); i++) {
            CourseEntity c1 = courses.get(i);
            int start1 = c1.getStartTime().getHour() * 60 + c1.getStartTime().getMinute();
            int end1 = start1 + extractDurationMinutes(c1);

            for (int j = i + 1; j < courses.size(); j++) {
                CourseEntity c2 = courses.get(j);
                if (!c1.getDate().equals(c2.getDate())) continue;

                int start2 = c2.getStartTime().getHour() * 60 + c2.getStartTime().getMinute();
                int end2 = start2 + extractDurationMinutes(c2);

                if (start1 < end2 && start2 < end1) {
                    conflicts.add(c1);
                    conflicts.add(c2);
                }
            }
        }
        return conflicts;
    }

    // ==========================================================
    // Course Layout and Rendering
    // ==========================================================

    private void buildPlanningCards() {
        sourceCourseCards.clear();
        LocalDate endOfWeek = selectedWeekMonday.plusDays(6);

        for (CourseEntity course : currentActiveCourses) {
            if (course.getDate().isBefore(selectedWeekMonday) || course.getDate().isAfter(endOfWeek)) {
                continue;
            }

            int dayIndex = (int) ChronoUnit.DAYS.between(selectedWeekMonday, course.getDate());
            if (dayIndex < 0 || dayIndex >= 6) continue;

            int startHour = course.getStartTime().getHour();
            int startMinute = course.getStartTime().getMinute();
            int durationMinutes = extractDurationMinutes(course);

            boolean isConflict = allConflictingCourses.contains(course);
            
            String moduleName = course.getModule() != null ? course.getModule().getName() : "Cours";
            String courseType = course.getCourseType() != null ? course.getCourseType().name() : "Type non défini";
            String room = course.getRoom() != null ? course.getRoom().getName() : "Pas de salle";
            
            String teacher = (course.getProfessors() != null && !course.getProfessors().isEmpty())
                    ? course.getProfessors().iterator().next().getFirstName() + " " + course.getProfessors().iterator().next().getLastName()
                    : "Prof non défini";

            addCourseCard(dayIndex, startHour, startMinute, durationMinutes, moduleName, courseType, teacher, room, isConflict);
        }
        
        layoutCoursesStacked();
    }

    private void addCourseCard(int dayIndex, int startHour, int startMinute, int durationMinutes,
                           String title, String type, String teacher, String room, boolean isConflict) {

        VBox card = buildCourseCardUI(title, type, teacher, room, isConflict);

        card.getProperties().put("dayIndex", dayIndex);
        card.getProperties().put("startMinutes", startHour * 60 + startMinute);
        card.getProperties().put("endMinutes", startHour * 60 + startMinute + durationMinutes);
        card.getProperties().put("durationMinutes", durationMinutes);

        sourceCourseCards.add(card);
    }

    private VBox buildCourseCardUI(String title, String type, String teacher, String room, boolean isConflict) {
        VBox card = new VBox(2);
        card.getStyleClass().add("course-card");
        card.setFillWidth(true);
        card.setAlignment(Pos.TOP_LEFT);
        card.setManaged(false);

        if (isConflict) {
            card.setStyle("-fx-background-color: #F44336; -fx-border-color: #D32F2F;");
        } else {
            card.setStyle("-fx-background-color: #4CAF50; -fx-border-color: #388E3C;");
        }

        Label t = createSingleLineLabel(title, "course-title");
        t.setStyle("-fx-text-fill: white; -fx-font-weight: bold;"); 
        
        Label ty = createSingleLineLabel(type, "course-meta");
        ty.setStyle("-fx-text-fill: white;");
        
        Label te = createSingleLineLabel(teacher, "course-meta");
        te.setStyle("-fx-text-fill: white;");
        
        Label r = createSingleLineLabel(room, "course-meta");
        r.setStyle("-fx-text-fill: white;");

        Button button = new Button("More");
        button.getStyleClass().add("button-more");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(20);
        button.setMinHeight(20);
        button.setFocusTraversable(false);

        t.setTooltip(new Tooltip(title));
        ty.setTooltip(new Tooltip(type));
        te.setTooltip(new Tooltip(teacher));
        r.setTooltip(new Tooltip(room));

        card.getChildren().addAll(t, ty, te, r, button);

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(card.widthProperty());
        clip.heightProperty().bind(card.heightProperty());
        card.setClip(clip);

        return card;
    }

    private Label createSingleLineLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        label.setWrapText(false);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setEllipsisString("...");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private void layoutCoursesStacked() {
        if (planningGrid.getWidth() <= 0) return;
        coursesPane.getChildren().clear();

        int dayCols = 6;
        for (int day = 0; day < dayCols; day++) {
            final int currentDay = day;

            List<Node> dayCards = sourceCourseCards.stream()
                    .filter(n -> ((int) n.getProperties().get("dayIndex")) == currentDay)
                    .sorted(java.util.Comparator.comparingInt(n -> (int) n.getProperties().get("startMinutes")))
                    .map(n -> (Node) n)
                    .toList();

            List<List<Node>> groups = buildOverlapGroups(dayCards);

            for (List<Node> group : groups) {
                if (group.size() == 1) {
                    VBox card = (VBox) group.get(0);
                    placeSingleCard(card, currentDay);
                    coursesPane.getChildren().add(card);
                } else {
                    Pane overlapPane = buildOverlapPane(group, currentDay);
                    coursesPane.getChildren().add(overlapPane);
                }
            }
        }
    }

    private List<List<Node>> buildOverlapGroups(List<Node> cards) {
        List<List<Node>> groups = new ArrayList<>();
        if (cards.isEmpty()) return groups;

        List<Node> currentGroup = new ArrayList<>();
        int currentMaxEnd = -1;

        for (Node card : cards) {
            int start = (int) card.getProperties().get("startMinutes");
            int end = (int) card.getProperties().get("endMinutes");

            if (currentGroup.isEmpty()) {
                currentGroup.add(card);
                currentMaxEnd = end;
            } else if (start < currentMaxEnd) {
                currentGroup.add(card);
                currentMaxEnd = Math.max(currentMaxEnd, end);
            } else {
                groups.add(currentGroup);
                currentGroup = new java.util.ArrayList<>();
                currentGroup.add(card);
                currentMaxEnd = end;
            }
        }

        if (!currentGroup.isEmpty()) {
            groups.add(currentGroup);
        }

        return groups;
    }

    private void placeSingleCard(VBox card, int dayIndex) {
        double horizontalPadding = 6;
        int start = (int) card.getProperties().get("startMinutes");
        int duration = (int) card.getProperties().get("durationMinutes");

        double columnX = getDayColumnX(dayIndex);
        double columnWidth = getDayColumnWidth();

        double x = columnX + horizontalPadding;
        double y = computeCourseY(start);
        double w = columnWidth - 2 * horizontalPadding;
        double h = computeCourseHeight(duration);

        if (h < 8) h = 8;

        card.setManaged(false);
        card.resizeRelocate(x, y, w, h);
    }

    private Pane buildOverlapPane(List<Node> group, int dayIndex) {
        Pane container = new Pane();

        double horizontalPadding = 6;
        double tabWidth = 18;
        double gap = 4;

        int minStart = group.stream().mapToInt(n -> (int) n.getProperties().get("startMinutes")).min().orElse(GRID_START_HOUR * 60);
        int maxEnd = group.stream().mapToInt(n -> (int) n.getProperties().get("endMinutes")).max().orElse(minStart + 60);

        double columnX = getDayColumnX(dayIndex);
        double columnWidth = getDayColumnWidth();

        double totalWidth = columnWidth - 2 * horizontalPadding;
        double cardWidth = totalWidth - tabWidth - gap;

        double x = columnX + horizontalPadding;
        double y = computeCourseY(minStart);
        double h = computeCourseHeight(maxEnd - minStart);

        if (h < ROW_HEIGHT) h = ROW_HEIGHT;

        container.setManaged(false);
        container.setLayoutX(x);
        container.setLayoutY(y);
        container.setPrefSize(totalWidth, h);
        container.setMinSize(totalWidth, h);
        container.setMaxSize(totalWidth, h);

        Pane cardsLayer = new Pane();
        cardsLayer.setManaged(false);
        cardsLayer.setLayoutX(0);
        cardsLayer.setLayoutY(0);
        cardsLayer.setPrefSize(cardWidth, h);
        cardsLayer.setMinSize(cardWidth, h);
        cardsLayer.setMaxSize(cardWidth, h);

        Pane selector = new Pane();
        selector.setManaged(false);
        selector.setLayoutX(cardWidth + gap);
        selector.setLayoutY(0);
        selector.setPrefSize(tabWidth, h);
        selector.setMinSize(tabWidth, h);
        selector.setMaxSize(tabWidth, h);

        container.setClip(new Rectangle(totalWidth, h));
        cardsLayer.setClip(new Rectangle(cardWidth, h));
        selector.setClip(new Rectangle(tabWidth, h));

        List<Button> tabButtons = new ArrayList<>();
        double nextAvailableTabY = 6.0; 

        for (int i = 0; i < group.size(); i++) {
            VBox card = (VBox) group.get(i);

            int start = (int) card.getProperties().get("startMinutes");
            int duration = (int) card.getProperties().get("durationMinutes");

            double innerY = computeCourseY(start) - y;
            double innerH = computeCourseHeight(duration);

            if (innerH < ROW_HEIGHT) innerH = ROW_HEIGHT;

            card.getProperties().put("innerY", innerY);
            card.getProperties().put("innerH", innerH);

            card.setManaged(false);
            card.resizeRelocate(0, innerY, cardWidth, innerH);

            if (i == 0) {
                card.setOpacity(1.0);
                card.setTranslateX(0);
                card.setTranslateY(0);
            } else {
                card.setOpacity(0.35);
                card.setTranslateX(6);
                card.setTranslateY(6);
            }

            cardsLayer.getChildren().add(card);

            Button indexBtn = new Button(String.valueOf(i + 1));
            indexBtn.getStyleClass().add("overlap-tab-button");
            indexBtn.setMinSize(tabWidth, 18);
            indexBtn.setPrefSize(tabWidth, 18);
            indexBtn.setMaxSize(tabWidth, 18);
            
            double desiredY = innerY + 6;
            double actualY = Math.max(desiredY, nextAvailableTabY);
            
            indexBtn.setLayoutX(0);
            indexBtn.setLayoutY(actualY);
            
            nextAvailableTabY = actualY + 22; 

            final int selectedIndex = i;
            indexBtn.setOnAction(e -> showCardInStack(cardsLayer, tabButtons, group, selectedIndex));

            tabButtons.add(indexBtn);
            selector.getChildren().add(indexBtn);
        }

        container.getChildren().addAll(cardsLayer, selector);
        showCardInStack(cardsLayer, tabButtons, group, 0);

        return container;
    }

    private void showCardInStack(Pane cardsLayer, List<Button> tabButtons, List<Node> group, int selectedIndex) {
        cardsLayer.getChildren().clear();

        for (int i = 0; i < group.size(); i++) {
            VBox card = (VBox) group.get(i);
            card.setTranslateX(0);
            card.setTranslateY(0);
            card.setOpacity(1.0);

            if (i != selectedIndex) {
                card.setOpacity(0.35);
                card.setTranslateX(6);
                card.setTranslateY(6);
            }
        }

        for (int i = 0; i < group.size(); i++) {
            if (i != selectedIndex) cardsLayer.getChildren().add(group.get(i));
        }
        cardsLayer.getChildren().add(group.get(selectedIndex));

        for (int i = 0; i < tabButtons.size(); i++) {
            Button btn = tabButtons.get(i);
            btn.getStyleClass().removeAll("overlap-tab-active", "overlap-tab-inactive");
            btn.getStyleClass().add(i == selectedIndex ? "overlap-tab-active" : "overlap-tab-inactive");
        }
    }

    private double getDayColumnWidth() {
        return planningGrid.getWidth() / 6.0;
    }

    private double getDayColumnX(int dayIndex) {
        return dayIndex * getDayColumnWidth();
    }

    private double computeCourseY(int startMinutes) {
        return ((startMinutes - GRID_START_HOUR * 60) / (double) SLOT_MINUTES) * ROW_HEIGHT;
    }

    private double computeCourseHeight(int durationMinutes) {
        return (durationMinutes / (double) SLOT_MINUTES) * ROW_HEIGHT;
    }

    private int extractDurationMinutes(CourseEntity course) {
        if (course.getDuration() == null) return 0;
        return (int) course.getDuration().toMinutes(); 
    }

    private String formatRange(LocalDate start, LocalDate end) {
        String startStr = start.format(dayMonthFmt);
        String endStr = end.format(dayMonthFmt);

        if (start.getMonth() == end.getMonth()) {
            String month = endStr.replaceAll("^\\d+\\s+", "");
            String startDay = String.valueOf(start.getDayOfMonth());
            String endDay = String.valueOf(end.getDayOfMonth());
            return startDay + " - " + endDay + " " + month;
        }

        return startStr + " - " + endStr;
    }

    @FXML
    private void onNextWeeks() {
        baseWeekMonday = baseWeekMonday.plusWeeks(weeksShown);
        selectedWeekMonday = selectedWeekMonday.plusWeeks(weeksShown);
        renderWeeksInto(weeksContainer, baseWeekMonday);
        buildPlanningCards(); 
    }

    @FXML
    private void onPrevWeeks() {
        baseWeekMonday = baseWeekMonday.minusWeeks(weeksShown);
        selectedWeekMonday = selectedWeekMonday.minusWeeks(weeksShown);
        renderWeeksInto(weeksContainer, baseWeekMonday);
        buildPlanningCards();
    }

    private void renderWeeksInto(HBox container, LocalDate baseMonday) {
        container.getChildren().clear();
        for (int i = 0; i < weeksShown; i++) {
            LocalDate weekMonday = baseMonday.plusWeeks(i);
            LocalDate weekSunday = weekMonday.plusDays(6);

            int weekNumber = weekMonday.get(weekFields.weekOfWeekBasedYear());
            String text = "S" + weekNumber + " (" + formatRange(weekMonday, weekSunday) + ")";

            Button weekBtn = new Button(text);
            weekBtn.getStyleClass().add("week-pill");
            weekBtn.setPrefWidth(140);
            weekBtn.setMinWidth(140);
            weekBtn.setMaxWidth(140);

            boolean hasConflictThisWeek = false;
            for (CourseEntity c : allConflictingCourses) {
                if (!c.getDate().isBefore(weekMonday) && !c.getDate().isAfter(weekSunday)) {
                    hasConflictThisWeek = true;
                    break;
                }
            }

            if (weekMonday.equals(selectedWeekMonday)) {
                weekBtn.getStyleClass().add("week-pill-active");
            } else if (hasConflictThisWeek) {
                weekBtn.setStyle("-fx-background-color: #ffebee; -fx-border-color: #f44336; -fx-text-fill: #c62828; -fx-background-radius: 15; -fx-border-radius: 15;");
            }

            weekBtn.setOnAction(e -> {
                selectedWeekMonday = weekMonday;
                renderWeeksInto(container, baseMonday);
                buildPlanningCards(); 
            });

            container.getChildren().add(weekBtn);
        }
    }

    private int computeWeeksShown() {
        double viewportWidth = weeksViewport.getWidth();
        if (viewportWidth <= 0) return 6;
        return Math.max(4, (int) Math.floor((viewportWidth + 12) / 152));
    }

    private LocalDate mondayOf(LocalDate date){
        int dow = date.getDayOfWeek().getValue();
        return date.minusDays(dow - 1L);
    }

    private void applyWeeksClip(){
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(weeksViewport.widthProperty());
        clip.heightProperty().bind(weeksViewport.heightProperty());
        weeksViewport.setClip(clip);
    }

    private void buildEmptyGrid(int startHour, int endHour, int stepHours) {
        planningGrid.getChildren().clear();
        timeColumn.getChildren().clear();

        int dayCols = 6;
        int startMinutes = GRID_START_HOUR * 60;
        int endMinutes = 20 * 60;
        int rows = (endMinutes - startMinutes) / SLOT_MINUTES;

        for (int r = 0; r < rows; r++) {
            int minutes = startMinutes + r * SLOT_MINUTES;
            int hour = minutes / 60;
            int min = minutes % 60;

            Label time = new Label(min == 0 ? String.format("%d:00", hour) : "");
            time.getStyleClass().add("time-label");
            time.setAlignment(Pos.CENTER_RIGHT);
            time.setPadding(new Insets(0, 12, 0, 0));
            time.setMinHeight(ROW_HEIGHT);
            time.setPrefHeight(ROW_HEIGHT);
            timeColumn.getChildren().add(time);

            for (int c = 0; c < dayCols; c++) {
                Pane cell = new Pane();
                cell.getStyleClass().add(c == 0 ? "planning-cell" : "planning-cell-inner");
                cell.setMinHeight(ROW_HEIGHT);
                cell.setPrefHeight(ROW_HEIGHT);
                cell.setMouseTransparent(true);
                planningGrid.add(cell, c, r);
            }
        }
        
        double totalHeight = rows * ROW_HEIGHT;
        coursesPane.setMinHeight(totalHeight);
        coursesPane.setPrefHeight(totalHeight);
        planningContent.setMinHeight(totalHeight);
    }

    @FXML
    private void handleLogout() {
        if (logoutButton != null) {
            logoutButton.setText("Chargement...");
            logoutButton.setDisable(true);
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/connexion-view.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/css/connexion.css").toExternalForm());
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            if (logoutButton != null) {
                logoutButton.setText("Déconnexion");
                logoutButton.setDisable(false);
            }
        }
    }
}