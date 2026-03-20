package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.entity.planning.Course;
import fr.univtln.projet.planning.service.planningService.CourseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.Node;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.List;

public class PlanningController {

    @FXML private Label userNameLabel;
    @FXML private GridPane planningGrid;
    @FXML private HBox weeksContainer;
    @FXML private Button prevWeeksBtn;
    @FXML private StackPane weeksViewport;
    @FXML private ToggleButton btnMonPlan;
    @FXML private ToggleButton btnMaPromo;
    @FXML private ToggleButton btnAutrePromo;
    @FXML private VBox timeColumn;
    @FXML private ScrollPane planningScroll;
    @FXML private ScrollPane timeScroll;
    @FXML private Pane coursesPane;
    @FXML private StackPane planningContent;

    private static final int GRID_START_HOUR = 8;
    private static final int SLOT_MINUTES = 30;
    private static final double ROW_HEIGHT = 60;

    private final ToggleGroup viewGroup = new ToggleGroup();

    private boolean weeksAnimating = false;
    private int weeksShown = 6;;

    private LocalDate baseWeekMonday;

    private LocalDate selectedWeekMonday;

    private final WeekFields weekFields = WeekFields.ISO;
    private final DateTimeFormatter dayMonthFmt = DateTimeFormatter.ofPattern("d MMM", Locale.FRENCH);

    private final List<VBox> sourceCourseCards = new ArrayList<>();
    private final CourseService courseService = new CourseService();

    /**
     * Called by JavaFx after the FXML file has been loaded
     * setting the displayed user name
     *
     */
    @FXML
    public void initialize() {
        userNameLabel.setText("Thomas Dejean");
        selectedWeekMonday = mondayOf(LocalDate.now());
        baseWeekMonday = selectedWeekMonday;

        Platform.runLater(this::applyWeeksClip);
        renderWeeksInto(weeksContainer, baseWeekMonday);

        buildEmptyGrid(8, 20, 1);
        refreshPlanning();
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
        });
    }

    private void applyWeeksClip(){
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(weeksViewport.widthProperty());
        clip.heightProperty().bind(weeksViewport.heightProperty());
        weeksViewport.setClip(clip);
    }

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

    private void loadCoursesFromService(List<Course> courses) {
        for (Course course : courses) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    course.getStartTime(),
                    ZoneId.of("Europe/Paris")
            );

            int dayIndex = dateTime.getDayOfWeek().getValue() - 1; // lundi = 0
            int startHour = dateTime.getHour();
            int startMinute = dateTime.getMinute();
            int durationMinutes = (int) course.getDuration().toMinutes();

            String moduleName = course.getModule() != null
                    ? course.getModule().getName()
                    : "Cours";

            String courseType = course.getCourseType() != null
                    ? course.getCourseType().name()
                    : "Type non défini";

            String teacher = (course.getProfessors() != null && !course.getProfessors().isEmpty())
                    ? course.getProfessors().get(0).getFirstName() + " " + course.getProfessors().get(0).getLastName()
                    : "Prof non défini";

            String room = course.getRoom() != null
                    ? course.getRoom().getName()
                    : "Salle non définie";

            addCourse(dayIndex, startHour, startMinute, durationMinutes, moduleName, courseType, teacher, room);
        }
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
            time.setAlignment(Pos.TOP_RIGHT);
            time.setPadding(new Insets(8, 12, 0, 0));
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
        VBox card = new VBox(6);
        card.getStyleClass().add("course-card");

        Label t = new Label(title);
        t.getStyleClass().add("course-title");
        t.setWrapText(true);
        t.setMaxWidth(Double.MAX_VALUE);

        Label ty = new Label(type);
        ty.getStyleClass().add("course-meta");
        ty.setWrapText(true);
        ty.setMaxWidth(Double.MAX_VALUE);

        Label te = new Label(teacher);
        te.getStyleClass().add("course-meta");
        te.setWrapText(true);
        te.setMaxWidth(Double.MAX_VALUE);

        Label r = new Label(room);
        r.getStyleClass().add("course-meta");
        r.setWrapText(true);
        r.setMaxWidth(Double.MAX_VALUE);

        Button button = new Button("More");
        button.getStyleClass().add("button-more");
        button.setWrapText(true);
        button.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(t, ty, te, r, button);

        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);

        return card;
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

    private void refreshPlanning() {
        sourceCourseCards.clear();
        coursesPane.getChildren().clear();

        List<Course> courses = courseService.getCoursesForWeek(selectedWeekMonday);
        loadCoursesFromService(courses);
        layoutCoursesStacked();
    }
    private void placeSingleCard(VBox card, double dayWidth, int dayIndex) {
        double horizontalPadding = 8;

        int start = (int) card.getProperties().get("startMinutes");
        int duration = (int) card.getProperties().get("durationMinutes");

        double x = dayIndex * dayWidth + horizontalPadding;
        double topMargin = (start == GRID_START_HOUR * 60) ? 12 : 0;
        double y = ((start - GRID_START_HOUR * 60) / (double) SLOT_MINUTES) * ROW_HEIGHT + topMargin;
        double h = (duration / (double) SLOT_MINUTES) * ROW_HEIGHT - topMargin;
        double w = dayWidth - 2 * horizontalPadding;

        card.setLayoutX(x);
        card.setLayoutY(y);
        card.setPrefWidth(w);
        card.setMinWidth(w);
        card.setMaxWidth(w);

        card.setPrefHeight(h);
        card.setMinHeight(h);
        card.setMaxHeight(h);
    }

    private Pane buildOverlapPane(List<Node> group, double dayWidth, int dayIndex) {
        Pane container = new Pane();

        double horizontalPadding = 8;
        double tabWidth = 18;
        double cardWidth = dayWidth - 2 * horizontalPadding;

        int minStart = group.stream()
                .mapToInt(n -> (int) n.getProperties().get("startMinutes"))
                .min()
                .orElse(GRID_START_HOUR * 60);

        int maxEnd = group.stream()
                .mapToInt(n -> (int) n.getProperties().get("endMinutes"))
                .max()
                .orElse(minStart + 60);

        double x = dayIndex * dayWidth + horizontalPadding;
        double topMargin = (minStart == GRID_START_HOUR * 60) ? 12 : 0;
        double y = ((minStart - GRID_START_HOUR * 60) / (double) SLOT_MINUTES) * ROW_HEIGHT + topMargin;
        double h = ((maxEnd - minStart) / (double) SLOT_MINUTES) * ROW_HEIGHT - topMargin;

        container.setLayoutX(x);
        container.setLayoutY(y);
        container.setPrefWidth(cardWidth + tabWidth);
        container.setMinWidth(cardWidth);
        container.setMaxWidth(cardWidth + tabWidth);

        container.setPrefHeight(h);
        container.setMinHeight(h);
        container.setMaxHeight(h);

        Pane cardsLayer = new Pane();
        cardsLayer.setLayoutX(0);
        cardsLayer.setLayoutY(0);
        cardsLayer.setPrefSize(cardWidth, h);
        cardsLayer.setMinSize(cardWidth, h);
        cardsLayer.setMaxSize(cardWidth, h);

        VBox selector = new VBox(4);
        selector.setLayoutX(cardWidth);
        selector.setLayoutY(0);
        selector.setPrefWidth(tabWidth);
        selector.setAlignment(Pos.TOP_LEFT);
        selector.setMouseTransparent(false);

        List<Button> tabButtons = new ArrayList<>();

        for (int i = 0; i < group.size(); i++) {
            VBox card = (VBox) group.get(i);

            int start = (int) card.getProperties().get("startMinutes");
            int duration = (int) card.getProperties().get("durationMinutes");

            double innerY = ((start - minStart) / (double) SLOT_MINUTES) * ROW_HEIGHT;
            double innerH = (duration / (double) SLOT_MINUTES) * ROW_HEIGHT;

            card.getProperties().put("innerY", innerY);
            card.getProperties().put("innerH", innerH);

            card.setLayoutX(0);
            card.setLayoutY(innerY);
            card.setPrefWidth(cardWidth);
            card.setMinWidth(cardWidth);
            card.setMaxWidth(cardWidth);

            card.setPrefHeight(innerH);
            card.setMinHeight(innerH);
            card.setMaxHeight(innerH);

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

            final int selectedIndex = i;
            indexBtn.setOnAction(e -> showCardInStack(cardsLayer, selector, tabButtons, group, selectedIndex));

            tabButtons.add(indexBtn);
            selector.getChildren().add(indexBtn);
        }

        container.getChildren().addAll(cardsLayer, selector);

        showCardInStack(cardsLayer, selector, tabButtons, group, 0);

        return container;
    }

    private void showCardInStack(Pane cardsLayer, VBox selector, List<Button> tabButtons, List<Node> group, int selectedIndex) {

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

        VBox selectedCard = (VBox) group.get(selectedIndex);
        double selectedY = (double) selectedCard.getProperties().get("innerY");
        selector.setLayoutY(selectedY + 6);

        for (int i = 0; i < tabButtons.size(); i++) {
            Button btn = tabButtons.get(i);
            btn.getStyleClass().removeAll("overlap-tab-active", "overlap-tab-inactive");
            btn.getStyleClass().add(i == selectedIndex ? "overlap-tab-active" : "overlap-tab-inactive");
        }
    }



    private void layoutCoursesStacked() {
        if (planningGrid.getWidth() <= 0) return;

        coursesPane.getChildren().clear();

        int dayCols = 6;
        double dayWidth = planningGrid.getWidth() / dayCols;

        for (int day = 0; day < dayCols; day++) {
            final int currentDay = day;

            java.util.List<Node> dayCards = sourceCourseCards.stream()
                    .filter(n -> ((int) n.getProperties().get("dayIndex")) == currentDay)
                    .sorted(java.util.Comparator.comparingInt(n -> (int) n.getProperties().get("startMinutes")))
                    .map(n -> (Node) n)
                    .toList();

            List<List<Node>> groups = buildOverlapGroups(dayCards);

            for (List<Node> group : groups) {
                if (group.size() == 1) {
                    VBox card = (VBox) group.get(0);
                    placeSingleCard(card, dayWidth, currentDay);
                    coursesPane.getChildren().add(card);
                } else {
                    Pane overlapPane = buildOverlapPane(group, dayWidth, currentDay);
                    coursesPane.getChildren().add(overlapPane);
                }
            }
        }
    }
}

