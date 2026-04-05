package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.modele.person.Admin;
import fr.univtln.projet.planning.modele.person.LocalStudent;
import fr.univtln.projet.planning.modele.person.Professor;
import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.service.planningService.CourseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Node;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.List;
import javafx.scene.control.OverrunStyle;
import fr.univtln.projet.planning.entity.planning.CourseEntity;

public class PlanningController {


    // ==========================================================
    // FXML
    // ==========================================================
    @FXML private Label userNameLabel;
    @FXML private GridPane planningGrid;
    @FXML private HBox weeksContainer;
    @FXML private StackPane weeksViewport;
    @FXML private ToggleButton btnMonPlan;
    @FXML private ToggleButton btnMaPromo;
    @FXML private ToggleButton btnAutrePromo;
    @FXML private VBox timeColumn;
    @FXML private ScrollPane planningScroll;
    @FXML private ScrollPane timeScroll;
    @FXML private Pane coursesPane;
    @FXML private StackPane planningContent;
    @FXML private Button logoutButton;

    private static final int GRID_START_HOUR = 8;
    private static final int SLOT_MINUTES = 30;
    private static final double ROW_HEIGHT = 60;



    // ==========================================================
    // Controller State
    // ==========================================================
    private final ToggleGroup viewGroup = new ToggleGroup();

    private boolean weeksAnimating = false;
    private int weeksShown = 6;;

    private LocalDate baseWeekMonday;

    private LocalDate selectedWeekMonday;

    private final WeekFields weekFields = WeekFields.ISO;
    private final DateTimeFormatter dayMonthFmt = DateTimeFormatter.ofPattern("d MMM", Locale.FRENCH);

    private final List<VBox> sourceCourseCards = new ArrayList<>();
    private CourseService courseService;
    private PlanningContext planningContext;


    // ==========================================================
    // Initialization
    // ==========================================================

    /**
     * Called by JavaFx after the FXML file has been loaded
     * setting the displayed user name
     *
     */
    @FXML
    public void initialize() {
        selectedWeekMonday = mondayOf(LocalDate.now());
        baseWeekMonday = selectedWeekMonday;

        Platform.runLater(this::applyWeeksClip);
        renderWeeksInto(weeksContainer, baseWeekMonday);

        buildEmptyGrid(8, 20, 1);

        btnMonPlan.setToggleGroup(viewGroup);
        btnMaPromo.setToggleGroup(viewGroup);
        btnAutrePromo.setToggleGroup(viewGroup);
        btnMaPromo.setSelected(true);

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
            layoutCoursesStacked();
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

    public void loadPlanning(PlanningContext planningContext) {
        this.planningContext = planningContext;
        refreshPlanning();
    }


    // ==========================================================
    // Week Navigation
    // ==========================================================



    @FXML
    private void onNextWeeks() {
        if (weeksAnimating) return;
        selectedWeekMonday = selectedWeekMonday.plusWeeks(1);
        slideWeeks(true);
        refreshPlanning();
    }

    @FXML
    private void onPrevWeeks() {
        if (weeksAnimating) return;
        selectedWeekMonday = selectedWeekMonday.minusWeeks(1);
        slideWeeks(false);
        refreshPlanning();
    }

    private void slideWeeks(boolean toNext) {
        weeksAnimating = true;

        double w = weeksViewport.getWidth();
        if (w <= 0) {
            w = 800;
        }

        LocalDate newBase = toNext
                ? baseWeekMonday.plusWeeks(weeksShown)
                : baseWeekMonday.minusWeeks(weeksShown);

        HBox incoming = new HBox(12);
        incoming.setAlignment(weeksContainer.getAlignment());
        renderWeeksInto(incoming, newBase);

        incoming.setTranslateX(toNext ? w : -w);

        HBox outgoing = weeksContainer;

        weeksViewport.getChildren().add(incoming);

        // animation duration
        Duration d = Duration.millis(1060);

        Timeline t = new Timeline(
                new KeyFrame(d,
                        new KeyValue(outgoing.translateXProperty(), toNext ? -w : w, Interpolator.EASE_BOTH),
                        new KeyValue(incoming.translateXProperty(), 0, Interpolator.EASE_BOTH)
                )
        );

        t.setOnFinished(e -> {
            weeksViewport.getChildren().remove(outgoing);
            incoming.setTranslateX(0);
            weeksContainer = incoming;

            baseWeekMonday = newBase;

            weeksAnimating = false;
        });

        t.play();
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

            if (weekMonday.equals(selectedWeekMonday)) {
                weekBtn.getStyleClass().add("week-pill-active");
            }

            weekBtn.setOnAction(e -> {
                selectedWeekMonday = weekMonday;
                renderWeeksInto(container, baseMonday);
                refreshPlanning();
            });

            container.getChildren().add(weekBtn);
        }
    }

    private int computeWeeksShown() {
        double viewportWidth = weeksViewport.getWidth();

        if (viewportWidth <= 0) {
            return 6;
        }

        double estimatedWeekButtonWidth = 115; // plus réaliste
        double gap = 12;

        int count = (int) Math.floor((viewportWidth + gap) / (estimatedWeekButtonWidth + gap));

        return Math.max(6, count);
    }


    // ==========================================================
    // Load Data
    // ==========================================================


    public void setConnectedUser(User user) {
        if (user == null) {
            userNameLabel.setText("Utilisateur inconnu");
            return;
        }

        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());

        if (user instanceof LocalStudent student) {
            loadPlanning(PlanningContext.forStudent(student.getUserId()));
        } else if (user instanceof Professor professor) {
            loadPlanning(PlanningContext.forProfessor(professor.getUserId()));
        } else if (user instanceof Admin admin) {
            userNameLabel.setText(userNameLabel.getText() + " (Admin)");
        } else {
            userNameLabel.setText(userNameLabel.getText() + " (type inconnu)");
        }
    }
    // A corriger proprement par la suite car la c'est une methode qui permet de recuperer la durée qui est en nanos secondes et pas en minutes
    private int extractDurationMinutes(CourseEntity course) {
        if (course.getDuration() == null) return 0;
        return (int) course.getDuration().toNanos();
    }

    private void loadCoursesFromService(List<CourseEntity> courses) {
        List<CourseEntity> uniqueCourses = courses.stream()
                .distinct()
                .toList();

        for (CourseEntity course : uniqueCourses) {
            int dayIndex = (int) ChronoUnit.DAYS.between(selectedWeekMonday, course.getDate());

            if (dayIndex < 0 || dayIndex >= 6) {
                continue;
            }

            int startHour = course.getStartTime().getHour();
            int startMinute = course.getStartTime().getMinute();
            int durationMinutes = extractDurationMinutes(course);


            String moduleName = course.getModule() != null
                    ? course.getModule().getName()
                    : "Cours";

            String courseType = course.getCourseType() != null
                    ? course.getCourseType().name()
                    : "Type non défini";

            String teacher = (course.getProfessors() != null && !course.getProfessors().isEmpty())
                    ? course.getProfessors().iterator().next().getFirstName() + " " +
                    course.getProfessors().iterator().next().getLastName()
                    : "Prof non défini";

            String room = course.getRoom() != null
                    ? course.getRoom().getName()
                    : "Salle non définie";

            addCourse(dayIndex, startHour, startMinute, durationMinutes, moduleName, courseType, teacher, room);
        }
    }

    private void refreshPlanning() {
        if (courseService == null || planningContext == null) return;

        sourceCourseCards.clear();
        coursesPane.getChildren().clear();

        LocalDate start = selectedWeekMonday;
        LocalDate end = selectedWeekMonday.plusDays(6);

        List<CourseEntity> courses = switch (planningContext.getType()) {
            case STUDENT -> courseService.findPlanningByStudentId(planningContext.getId(), start, end);
            case PROFESSOR -> courseService.findPlanningByProfessorId(planningContext.getId(), start, end);
            case GROUP -> courseService.findPlanningByGroupId(planningContext.getId(), start, end);
            case PROMO -> courseService.findPlanningByPromoId(planningContext.getId(), start, end);
            case ROOM -> courseService.findPlanningByRoomId(planningContext.getId(), start, end);
        };

        loadCoursesFromService(courses);
        layoutCoursesStacked();
    }


    // ==========================================================
    // Build Interface
    // ==========================================================

    private void buildEmptyGrid(int startHour, int endHour, int stepHours) {
        planningGrid.getChildren().clear();
        timeColumn.getChildren().clear();


        int dayCols = 6;
        int startMinutes = GRID_START_HOUR * 60;
        int endMinutes = 20 * 60;
        int rows = (endMinutes - startMinutes) / SLOT_MINUTES;
        double rowHeight = 60;

        for (int r = 0; r < rows; r++) {


            int minutes = startMinutes + r * SLOT_MINUTES;
            int hour = minutes / 60;
            int min = minutes % 60;


            Label time = new Label(min == 0 ? String.format("%d:00", hour) : "");
            time.getStyleClass().add("time-label");
            time.setAlignment(Pos.CENTER_RIGHT);
            time.setPadding(new Insets(0, 12, 0, 0));
            time.setMinHeight(rowHeight);
            time.setPrefHeight(rowHeight);
            time.setMaxHeight(rowHeight);
            time.setMaxWidth(Double.MAX_VALUE);
            timeColumn.getChildren().add(time);


            for (int c = 0; c < dayCols; c++) {
                Pane cell = new Pane();

                if (c == 0) {
                    cell.getStyleClass().add("planning-cell");
                } else {
                    cell.getStyleClass().addAll("planning-cell", "planning-cell-inner");
                }

                cell.setMinHeight(rowHeight);
                cell.setPrefHeight(rowHeight);
                cell.setMaxHeight(rowHeight);
                cell.setMaxWidth(Double.MAX_VALUE);
                cell.setMouseTransparent(true);
                planningGrid.add(cell, c, r);
            }

        }
        double totalHeight = rows * rowHeight;

        coursesPane.setMinHeight(totalHeight);
        coursesPane.setPrefHeight(totalHeight);
        coursesPane.setMaxHeight(totalHeight);
        planningContent.setMinHeight(totalHeight);
        planningContent.setPrefHeight(totalHeight);
        planningContent.setMaxHeight(totalHeight);

    }

    private VBox buildCourseCard(String title, String type, String teacher, String room) {
        VBox card = new VBox(2);
        card.getStyleClass().add("course-card");
        card.setFillWidth(true);
        card.setAlignment(Pos.TOP_LEFT);
        card.setManaged(false);

        Label t = createSingleLineLabel(title, "course-title");
        Label ty = createSingleLineLabel(type, "course-meta");
        Label te = createSingleLineLabel(teacher, "course-meta");
        Label r = createSingleLineLabel(room, "course-meta");

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


    private void addCourse(int dayIndex, int startHour, int startMinute, int durationMinutes,
                           String title, String type, String teacher, String room) {

        VBox card = buildCourseCard(title, type, teacher, room);

        card.getProperties().put("dayIndex", dayIndex);
        card.getProperties().put("startMinutes", startHour * 60 + startMinute);
        card.getProperties().put("endMinutes", startHour * 60 + startMinute + durationMinutes);
        card.getProperties().put("durationMinutes", durationMinutes);

        sourceCourseCards.add(card);
    }

    // ==========================================================
    // Course Layout and Rendering
    // ==========================================================

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

        int minStart = group.stream()
                .mapToInt(n -> (int) n.getProperties().get("startMinutes"))
                .min()
                .orElse(GRID_START_HOUR * 60);

        int maxEnd = group.stream()
                .mapToInt(n -> (int) n.getProperties().get("endMinutes"))
                .max()
                .orElse(minStart + 60);

        double columnX = getDayColumnX(dayIndex);
        double columnWidth = getDayColumnWidth();

        double totalWidth = columnWidth - 2 * horizontalPadding;
        double cardWidth = totalWidth - tabWidth - gap;

        double x = columnX + horizontalPadding;
        double y = computeCourseY(minStart);
        double h = computeCourseHeight(maxEnd - minStart);

        if (h < ROW_HEIGHT) {
            h = ROW_HEIGHT;
        }

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

        for (int i = 0; i < group.size(); i++) {
            VBox card = (VBox) group.get(i);

            int start = (int) card.getProperties().get("startMinutes");
            int duration = (int) card.getProperties().get("durationMinutes");

            double innerY = computeCourseY(start) - y;
            double innerH = computeCourseHeight(duration);

            if (innerH < ROW_HEIGHT) {
                innerH = ROW_HEIGHT;
            }

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

            indexBtn.setLayoutX(0);
            indexBtn.setLayoutY(innerY + 6);

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
            if (i != selectedIndex) {
                cardsLayer.getChildren().add(group.get(i));
            }
        }

        cardsLayer.getChildren().add(group.get(selectedIndex));

        for (int i = 0; i < tabButtons.size(); i++) {
            Button btn = tabButtons.get(i);
            btn.getStyleClass().removeAll("overlap-tab-active", "overlap-tab-inactive");
            btn.getStyleClass().add(i == selectedIndex ? "overlap-tab-active" : "overlap-tab-inactive");
        }
    }



    private void layoutCoursesStacked() {
        if (planningGrid.getWidth() <= 0 || planningGrid.getHeight() <= 0) return;

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


    // ==========================================================
    // Calcul
    // ==========================================================


    private LocalDate mondayOf(LocalDate date){
        int dow = date.getDayOfWeek().getValue();
        return date.minusDays(dow - 1L);
    }


    private String formatRange(LocalDate start, LocalDate end) {
        String startStr = start.format(dayMonthFmt);
        String endStr = end.format(dayMonthFmt);

        // if the month is the same, we can shorten it
        if (start.getMonth() == end.getMonth()) {
            String month = endStr.replaceAll("^\\d+\\s+", "");
            String startDay = String.valueOf(start.getDayOfMonth());
            String endDay = String.valueOf(end.getDayOfMonth());
            return startDay + " - " + endDay + " " + month;
        }

        return startStr + " - " + endStr;
    }

    private void applyWeeksClip(){
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(weeksViewport.widthProperty());
        clip.heightProperty().bind(weeksViewport.heightProperty());
        weeksViewport.setClip(clip);
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



    @FXML
    private void handleLogout() {
        logoutButton.setText("Chargement...");
        logoutButton.setDisable(true);
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/connexion-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass().getResource("/css/connexion.css").toExternalForm()
            );

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            logoutButton.setText("Déconnexion");
            logoutButton.setDisable(false);
        }
    }
}