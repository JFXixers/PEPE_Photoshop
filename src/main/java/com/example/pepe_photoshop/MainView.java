package com.example.pepe_photoshop;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MainView {

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private ImageView ImageView;

    @FXML
    public void loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.bmp"));

        Image image = new Image(fileChooser.showOpenDialog(AnchorPane.getScene().getWindow()).toURI().toString());

        ImageView.setFitHeight(image.getHeight());
        ImageView.setFitWidth(image.getWidth());
        ImageView.setImage(image);
    }
}
