package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.service.academicService.PromoService;
import fr.univtln.projet.planning.service.planningService.CourseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;

public class MainController {

    @FXML private StackPane contentArea;
    @FXML private SidebarController sidebarController;
    @FXML private VBox sidebar;


    private CourseService courseService;
    private PromoService promoService;
    private User connectedUser;
    private boolean guestMode = false;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setOnMenuSelected(this::handleSidebarNavigation);
        }
    }

    public void setGuestMode(boolean guestMode) {
        this.guestMode = guestMode;
    }
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setPromoService(PromoService promoService) {
        this.promoService = promoService;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;

        if (sidebarController != null && connectedUser != null) {
            sidebarController.setUserName(connectedUser.getFirstName() + " " + connectedUser.getLastName());
        }
    }

    public void initGuestData() {
        loadPlanningView();

        if (sidebarController != null) {
            sidebarController.setUserName("Invité");
            sidebarController.setActiveById("btnPlanning");
        }
    }

    public void initData() {
        loadPlanningView();

        if (sidebarController != null) {
            sidebarController.setActiveById("btnPlanning");
        }
    }

    private void handleSidebarNavigation(String itemId) {
        System.out.println("Navigation demandée : " + itemId);
        switch (itemId) {


            case "btnPlanning" -> {
                loadPlanningView();
                sidebarController.setActiveById("btnPlanning");
            }
            case "btnMap" -> {
                loadView("/view/map-view.fxml");
                sidebarController.setActiveById("btnMap");
            }
            case "btnLogout" -> logout();

            case "btnRooms" -> {
                loadView("/view/room-view.fxml");
                sidebarController.setActiveById("btnRooms");
            }
        }
    }

    private void loadPlanningView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/planning-view.fxml")
            );

            Node view = loader.load();

            PlanningController planningController = loader.getController();
            planningController.setCourseService(courseService);
            planningController.setPromoService(promoService);

            if (guestMode) {
                planningController.setGuestMode(true);
            } else {
                planningController.setConnectedUser(connectedUser);
            }

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/connexion-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass().getResource("/css/connexion.css").toExternalForm()
            );

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}