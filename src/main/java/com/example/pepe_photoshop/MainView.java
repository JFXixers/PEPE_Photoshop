package com.example.pepe_photoshop;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;

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

        ImageView.setImage(image);
    }

    @FXML
    public void saveImage() {
        Image imageToSave = ImageView.getImage();

        FileChooser fileChooserForSave = new FileChooser();
        fileChooserForSave.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.bmp", "png"));
        fileChooserForSave.setTitle("Save image");
        File fileToSave = fileChooserForSave.showSaveDialog(AnchorPane.getScene().getWindow());
        String nameOfFileToSafe = fileToSave.getName();

        int indexOfDot = nameOfFileToSafe.lastIndexOf(".");
        String format = nameOfFileToSafe.substring(indexOfDot+1);

        BufferedImage buferedImg = SwingFXUtils.fromFXImage(imageToSave, null);
        try {
            ImageIO.write(buferedImg, format, fileToSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
