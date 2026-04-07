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

import fr.univtln.projet.planning.entity.academic.GroupEntity;
import fr.univtln.projet.planning.modele.academic.GroupType;
import fr.univtln.projet.planning.entity.planning.CourseEntity;
import fr.univtln.projet.planning.entity.planning.ModuleEntity;
import fr.univtln.projet.planning.modele.person.InternationalStudent;
import fr.univtln.projet.planning.service.planningService.CourseService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class DRIController {

    @FXML private Label studentTitleLabel;
    @FXML private Label userNameLabel;
    @FXML private Label ectsLabel;
    @FXML private Label conflictsLabel;
    
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
    
    private InternationalStudent currentStudent;
    private List<ModuleEntity> inputModules = new ArrayList<>();
    
    // Pour un module, on peut avoir un groupe PROMO obligatoire + 1 groupe TD/TP au choix
    private final Map<String, GroupEntity> promoGroupsPerModule = new HashMap<>();
    private final Map<String, GroupEntity> selectedSubGroupsPerModule = new HashMap<>();
    private final Map<String, Boolean> moduleSelectionStatus = new HashMap<>();
    
    private final List<CourseEntity> allAcademicYearCourses = new ArrayList<>();
    private List<CourseEntity> currentActiveCourses = new ArrayList<>();
    private Set<CourseEntity> allConflictingCourses = new HashSet<>();

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
        planningGrid.widthProperty().addListener((obs, oldVal, newVal) -> renderPlanning());

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

    public void loadStudentDRI(InternationalStudent student, List<ModuleEntity> modules) {
        this.currentStudent = student;
        this.inputModules = modules;
        
        if (studentTitleLabel != null) {
            studentTitleLabel.setText("Planning de l'étudiant(e) : " + student.getFirstName() + " " + student.getLastName());
        }
        
        for (ModuleEntity m : modules) {
            moduleSelectionStatus.put(m.getCode(), true);
        }

        buildBasket();
        loadAllAcademicYearCourses(); 
    }

    private void buildBasket() {
        if (basketContainer == null) return;
        basketContainer.getChildren().clear();

        for (ModuleEntity module : inputModules) {
            VBox card = new VBox();
            card.setStyle("-fx-border-color: #d1d1d1; -fx-background-color: #f9f9f9;");

            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(new Insets(10));
            
            CheckBox activeCheck = new CheckBox();
            activeCheck.setSelected(true);
            activeCheck.setOnAction(e -> {
                moduleSelectionStatus.put(module.getCode(), activeCheck.isSelected());
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

            Map<Integer, GroupEntity> uniqueGroupsMap = new HashMap<>();
            
            if (courseService != null) {
                List<CourseEntity> moduleCourses = courseService.findPlanningByModuleCode(
                        module.getCode(), 
                        startOfAcademicYear, 
                        endOfAcademicYear
                );
                
                if (moduleCourses != null) {
                    for (CourseEntity c : moduleCourses) {
                        if (c.getGroups() != null) {
                            for (GroupEntity g : c.getGroups()) {
                                uniqueGroupsMap.put(g.getNum(), g);
                            }
                        }
                    }
                }
            }
            
            List<GroupEntity> availableGroups = new ArrayList<>(uniqueGroupsMap.values());

            // Séparer le groupe PROMO des autres groupes (TD/TP)
            List<GroupEntity> subGroups = new ArrayList<>();
            for (GroupEntity g : availableGroups) {
                if (g.getType() == GroupType.PROMO) {
                    promoGroupsPerModule.put(module.getCode(), g); // On le garde en mémoire silencieusement
                } else {
                    subGroups.add(g); // On garde les autres pour les boutons radios
                }
            }

            if (!subGroups.isEmpty()) {
                Label grpTitle = new Label("Groupes TD/TP");
                grpTitle.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");
                body.getChildren().add(grpTitle);

                ToggleGroup groupToggle = new ToggleGroup();
                boolean first = true;
                
                for (GroupEntity grp : subGroups) {
                    RadioButton rb = new RadioButton(grp.getPromo().getName() + " " + 
                                        grp.getPromo().getStudyLevel() + " - Groupe " + 
                                        grp.getNum() + " (" + grp.getType() + ")");
                    rb.setToggleGroup(groupToggle);
                    if (first) {
                        rb.setSelected(true);
                        selectedSubGroupsPerModule.put(module.getCode(), grp);
                        first = false;
                    }
                    
                    rb.setOnAction(e -> {
                        if (rb.isSelected()) {
                            selectedSubGroupsPerModule.put(module.getCode(), grp);
                            updateDRIState();
                        }
                    });
                    body.getChildren().add(rb);
                }
            } else if (promoGroupsPerModule.containsKey(module.getCode())) {
                body.getChildren().add(new Label("CM Uniquement (Géré automatiquement)"));
            } else {
                body.getChildren().add(new Label("Aucun groupe disponible"));
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
        renderPlanning();
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

    /**
     * Garde le cours si :
     * 1. Il n'a aucun groupe (cours générique)
     * 2. Il appartient au groupe PROMO de ce module
     * 3. Il appartient au sous-groupe (TD/TP) sélectionné par l'utilisateur
     */
    private List<CourseEntity> getActiveCourses(List<CourseEntity> rawCourses) {
        List<CourseEntity> active = new ArrayList<>();
        for (CourseEntity course : rawCourses) {
            ModuleEntity module = course.getModule();
            if (module == null || !moduleSelectionStatus.getOrDefault(module.getCode(), false)) continue;

            GroupEntity promoGroup = promoGroupsPerModule.get(module.getCode());
            GroupEntity selectedSubGroup = selectedSubGroupsPerModule.get(module.getCode());
            boolean keepCourse = false;
            
            if (course.getGroups() == null || course.getGroups().isEmpty()) {
                keepCourse = true; 
            } else {
                for (GroupEntity cg : course.getGroups()) {
                    // Est-ce le groupe PROMO obligatoire ?
                    boolean isPromo = (promoGroup != null && cg.getNum() == promoGroup.getNum() 
                            && Objects.equals(cg.getType(), promoGroup.getType()) 
                            && Objects.equals(cg.getPromo().getName(), promoGroup.getPromo().getName()));
                    
                    // Est-ce le groupe TD/TP choisi par l'étudiant ?
                    boolean isSelectedSubGroup = (selectedSubGroup != null && cg.getNum() == selectedSubGroup.getNum() 
                            && Objects.equals(cg.getType(), selectedSubGroup.getType()) 
                            && Objects.equals(cg.getPromo().getName(), selectedSubGroup.getPromo().getName()));

                    if (isPromo || isSelectedSubGroup) {
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

    private void renderPlanning() {
        if (coursesPane == null) return;
        coursesPane.getChildren().clear();
        
        if (planningGrid.getWidth() <= 0) return;

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
            String courseType = course.getCourseType() != null ? course.getCourseType().name() : "";
            String room = course.getRoom() != null ? course.getRoom().getName() : "Pas de salle";

            VBox card = buildCourseCard(moduleName, courseType, room, isConflict);
            placeSingleCard(card, dayIndex, startHour * 60 + startMinute, durationMinutes);
            coursesPane.getChildren().add(card);
        }
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

    private VBox buildCourseCard(String title, String type, String room, boolean isConflict) {
        VBox card = new VBox(2);
        card.setFillWidth(true);
        card.setPadding(new Insets(5));
        
        if (isConflict) {
            card.setStyle("-fx-background-color: #F44336; -fx-background-radius: 4; -fx-border-color: #D32F2F; -fx-border-radius: 4;");
        } else {
            card.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 4; -fx-border-color: #388E3C; -fx-border-radius: 4;");
        }

        Label t = new Label(title);
        t.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 11px;");
        t.setWrapText(true);
        
        Label ty = new Label("Type: " + type + " | Salle: " + room);
        ty.setStyle("-fx-text-fill: white; -fx-font-size: 10px;");
        ty.setWrapText(true);

        card.getChildren().addAll(t, ty);
        return card;
    }

    private void placeSingleCard(VBox card, int dayIndex, int startMinutes, int duration) {
        double horizontalPadding = 4;
        double columnX = dayIndex * (planningGrid.getWidth() / 6.0);
        double columnWidth = (planningGrid.getWidth() / 6.0);

        double x = columnX + horizontalPadding;
        double y = ((startMinutes - GRID_START_HOUR * 60) / (double) SLOT_MINUTES) * ROW_HEIGHT;
        double w = columnWidth - 2 * horizontalPadding;
        double h = (duration / (double) SLOT_MINUTES) * ROW_HEIGHT;

        if (h < 15) h = 15;

        card.setManaged(false);
        card.resizeRelocate(x, y, w, h);
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
        updateDRIState(); 
    }

    @FXML
    private void onPrevWeeks() {
        baseWeekMonday = baseWeekMonday.minusWeeks(weeksShown);
        selectedWeekMonday = selectedWeekMonday.minusWeeks(weeksShown);
        renderWeeksInto(weeksContainer, baseWeekMonday);
        updateDRIState();
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
                updateDRIState(); 
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
}