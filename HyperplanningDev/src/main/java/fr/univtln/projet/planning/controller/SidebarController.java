package fr.univtln.projet.planning.controller;

import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

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

    // ... déclare les autres boutons ici

    private List<HBox> menuItems;

    @FXML
    public void initialize() {
        // On regroupe tous les boutons dans une liste pour faciliter leur gestion
        menuItems = Arrays.asList(btnUfr, btnPromo, btnPlanning, btnTeacher, btnStudent, btnRooms, btnMap, btnMenu);
    }

    /**
     * Méthode appelée à chaque clic sur un bouton du menu.
     */
    @FXML
    private void handleMenuClick(MouseEvent event) {
        // L'élément cliqué (la HBox)
        HBox clickedItem = (HBox) event.getSource();

        // 1. Enlever l'état "sélectionné" de TOUS les boutons
        for (HBox item : menuItems) {
            removeSelectedStyle(item);
        }

        // 2. Ajouter l'état "sélectionné" UNIQUEMENT au bouton cliqué
        addSelectedStyle(clickedItem);
        
        // Optionnel : Ici tu pourrais aussi déclencher le changement de page
        // changerPage(clickedItem.getId()); 
    }

    /**
     * Ajoute les classes CSS de sélection à la HBox et à ses enfants (SVG et Label)
     */
    private void addSelectedStyle(HBox item) {
    if (!item.getStyleClass().contains("nav-item-selected")) {
        item.getStyleClass().add("nav-item-selected");
    }
    for (Node child : item.getChildren()) {
        if (child instanceof Region && !(child instanceof HBox)) {
            child.getStyleClass().add("nav-icon-selected");
            child.setStyle("-fx-background-color: #1877d2;");
        } else if (child instanceof Label) {
            child.getStyleClass().add("nav-text-selected");
        }
    }
}

private void removeSelectedStyle(HBox item) {
    item.getStyleClass().remove("nav-item-selected");
    for (Node child : item.getChildren()) {
        if (child instanceof Region && !(child instanceof HBox)) {
            child.getStyleClass().remove("nav-icon-selected");
            child.setStyle("-fx-background-color: #6b7280;");
        } else if (child instanceof Label) {
            child.getStyleClass().remove("nav-text-selected");
        }
    }
}
}