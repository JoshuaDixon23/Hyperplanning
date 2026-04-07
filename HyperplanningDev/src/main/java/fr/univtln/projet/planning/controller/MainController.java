package fr.univtln.projet.planning.controller;

import fr.univtln.projet.planning.modele.person.User;
import fr.univtln.projet.planning.service.academicService.PromoService;
import fr.univtln.projet.planning.service.planningService.CourseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML private StackPane contentArea;
    @FXML private SidebarController sidebarController;

    private CourseService courseService;
    private PromoService promoService;
    private User connectedUser;

    @FXML
    public void initialize() {
        if (sidebarController != null) {
            sidebarController.setOnMenuSelected(this::handleSidebarNavigation);
        }
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

    public void initData() {
        loadPlanningView();

        if (sidebarController != null) {
            sidebarController.setActiveById("btnPlanning");
        }
    }

    private void handleSidebarNavigation(String itemId) {
        switch (itemId) {
            case "btnPlanning" -> {
                loadPlanningView();
                sidebarController.setActiveById("btnPlanning");
            }
            case "btnMap" -> {
                loadView("/view/map-view.fxml");
                sidebarController.setActiveById("btnMap");
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
            planningController.setConnectedUser(connectedUser);

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
}