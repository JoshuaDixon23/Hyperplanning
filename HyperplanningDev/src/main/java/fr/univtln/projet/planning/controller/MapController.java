package fr.univtln.projet.planning.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapController {

    @FXML private ImageView mapImageView;

    @FXML
    public void initialize() {
        Image image = new Image(
                getClass().getResource("/images/map.png").toExternalForm()
        );
        mapImageView.setImage(image);
    }
}