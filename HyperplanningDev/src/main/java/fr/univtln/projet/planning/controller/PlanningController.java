package fr.univtln.projet.planning.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.Node;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

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
    @FXML private VBox planningContent;
    @FXML private ScrollPane timeScroll;

    private final ToggleGroup viewGroup = new ToggleGroup();

    private boolean weeksAnimating = false;
    private static final int WEEKS_SHOWN = 6;;

    private LocalDate baseWeekMonday;

    private LocalDate selectedWeekMonday;

    private final WeekFields weekFields = WeekFields.ISO;
    private final DateTimeFormatter dayMonthFmt = DateTimeFormatter.ofPattern("d MMM", Locale.FRENCH);

    /**
     * Called by JavaFx after the FXML file has been loaded
     * setting the displayed user name
     *
     */
    @FXML
    public void initialize() {
        userNameLabel.setText("Thomas Dejean");
        selectedWeekMonday = mondayOf(LocalDate.now());
        baseWeekMonday= selectedWeekMonday;

        Platform.runLater(this::applyWeeksClip);
        renderWeeksInto(weeksContainer, baseWeekMonday);
        buildEmptyGrid(8, 20, 1);
        btnMonPlan.setToggleGroup(viewGroup);
        btnMaPromo.setToggleGroup(viewGroup);
        btnAutrePromo.setToggleGroup(viewGroup);

        btnMaPromo.setSelected(true);
        Platform.runLater(() -> {
            timeScroll.vvalueProperty().bindBidirectional(planningScroll.vvalueProperty());
        });
    }

    private void applyWeeksClip(){
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(weeksViewport.widthProperty());
        clip.heightProperty().bind(weeksViewport.heightProperty());
        weeksViewport.setClip(clip);
    }

    @FXML
    private void onNextWeeks(){
        if (weeksAnimating) return;
        slideWeeks(true);
    }

    @FXML
    private void onPrevWeeks() {
        if (weeksAnimating) return;
        slideWeeks(false);
    }

    private void slideWeeks(boolean toNext) {
        weeksAnimating = true;

        double w = weeksViewport.getWidth();
        if (w <= 0) {
            w = 800;
        }

        LocalDate newBase = toNext
                ? baseWeekMonday.plusWeeks(WEEKS_SHOWN)
                : baseWeekMonday.minusWeeks(WEEKS_SHOWN);

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

        for (int i = 0; i < WEEKS_SHOWN; i++) {
            LocalDate weekMonday = baseMonday.plusWeeks(i);
            LocalDate weekSunday = weekMonday.plusDays(6);

            int weekNumber = weekMonday.get(weekFields.weekOfWeekBasedYear());
            String text = "S" + weekNumber + " (" + formatRange(weekMonday, weekSunday) + ")";

            Button weekBtn = new Button(text);
            weekBtn.getStyleClass().add("week-pill");

            if (weekMonday.equals(selectedWeekMonday)) {
                weekBtn.getStyleClass().add("week-pill-active");
            }

            weekBtn.setOnAction(e -> {
                selectedWeekMonday = weekMonday;
                renderWeeksInto(container, baseMonday);
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
        int rows = (endHour - startHour) / stepHours;
        double rowHeight = 60;

        for (int r = 0; r < rows; r++) {

            int hour = startHour + (r * stepHours);

            Label time = new Label(hour + " : 00");
            time.getStyleClass().add("time-label");
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

        planningContent.setMinHeight(totalHeight);
        planningContent.setPrefHeight(totalHeight);
        planningContent.setMaxHeight(totalHeight);

    }


}