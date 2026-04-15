package fr.univtln.projet.planning.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class SidebarController {

    // Injection des HBox définies dans le FXML
    @FXML private HBox btnUfr;
    @FXML private HBox btnPromo;
    @FXML private HBox btnPlanning;
    @FXML private HBox btnTeacher;
    @FXML private HBox btnStudent;
    @FXML private HBox btnRooms;
    @FXML private HBox btnMap;
    @FXML private HBox btnMenu;
    @FXML private HBox btnDashboard;
    @FXML private HBox btnModules;
    @FXML private Label userNameLabel;


    // ... déclare les autres boutons ici

    private List<HBox> menuItems;
    private Consumer<String> onMenuSelected;

    @FXML
    public void initialize() {
        // On regroupe tous les boutons dans une liste pour faciliter leur gestion
        menuItems = Arrays.asList(
                btnUfr, btnPromo, btnPlanning, btnTeacher, btnStudent,
                btnRooms, btnMap, btnMenu, btnDashboard, btnModules
        ).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Méthode appelée à chaque clic sur un bouton du menu.
     */
    @FXML
    private void handleMenuClick(MouseEvent event) {
        // L'élément cliqué (la HBox)
        HBox clickedItem = (HBox) event.getSource();
        System.out.println("Clic sidebar : " + clickedItem.getId());

        if ("btnLogout".equals(clickedItem.getId())) {
            if (onMenuSelected != null) {
                onMenuSelected.accept(clickedItem.getId());
            }
            return;
        }

        // 1. Enlever l'état "sélectionné" de TOUS les boutons
        for (HBox item : menuItems) {
            removeSelectedStyle(item);
        }

        // 2. Ajouter l'état "sélectionné" UNIQUEMENT au bouton cliqué
        addSelectedStyle(clickedItem);


        // changerPage(clickedItem.getId());
        if (onMenuSelected != null) {
            onMenuSelected.accept(clickedItem.getId());
        }
    }

    /**
     * Ajoute les classes CSS de sélection à la HBox et à ses enfants (SVG et Label)
     */
    private void addSelectedStyle(HBox item) {
        if (!item.getStyleClass().contains("nav-item-selected")) {
            item.getStyleClass().add("nav-item-selected");
        }

        for (Node child : item.getChildren()) {
            if (child instanceof SVGPath || (child instanceof Region && !(child instanceof HBox))) {
                if (!child.getStyleClass().contains("nav-icon-selected")) {
                    child.getStyleClass().add("nav-icon-selected");
                }
            } else if (child instanceof Label label) {
                if (label.getStyleClass().contains("nav-text")
                        && !label.getStyleClass().contains("nav-text-selected")) {
                    label.getStyleClass().add("nav-text-selected");
                }
            }
        }
    }

    private void removeSelectedStyle(HBox item) {
        item.getStyleClass().remove("nav-item-selected");

        for (Node child : item.getChildren()) {
            if (child instanceof SVGPath || (child instanceof Region && !(child instanceof HBox))) {
                child.getStyleClass().remove("nav-icon-selected");
            } else if (child instanceof Label label) {
                label.getStyleClass().remove("nav-text-selected");
            }
        }
    }

    public void setUserName(String userName) {
        if (userNameLabel != null) {
            userNameLabel.setText(userName);
        }
    }

    public void setOnMenuSelected(Consumer<String> onMenuSelected) {
        this.onMenuSelected = onMenuSelected;
    }

    public void setActiveById(String itemId) {
        if (itemId == null) return;

        for (HBox item : menuItems) {
            removeSelectedStyle(item);
        }

        for (HBox item : menuItems) {
            if (itemId.equals(item.getId())) {
                addSelectedStyle(item);
                break;
            }
        }
    }






}