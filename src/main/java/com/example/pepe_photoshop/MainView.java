package com.example.pepe_photoshop;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;

public class MainView {

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private ImageView ImageView;

    public static Image originalImage;
    public static Image modifiedImage;

    @FXML
    public void loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.bmp"));

        Image image = new Image(fileChooser.showOpenDialog(AnchorPane.getScene().getWindow()).toURI().toString());

        ImageView.setImage(image);
        originalImage = ImageView.getImage();
        modifiedImage = null;
    }

    @FXML
    public void saveImage() {
        Image imageToSave = ImageView.getImage();

        FileChooser fileChooserForSave = new FileChooser();
        fileChooserForSave.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG images","*.png"));
        fileChooserForSave.setTitle("Save image");
        fileChooserForSave.setInitialFileName("my_image.png");
        File fileToSave = fileChooserForSave.showSaveDialog(AnchorPane.getScene().getWindow());
        String nameOfFileToSafe = fileToSave.getName();

        int indexOfDot = nameOfFileToSafe.lastIndexOf(".");
        String format = nameOfFileToSafe.substring(indexOfDot+1);

        BufferedImage bufferedImg = SwingFXUtils.fromFXImage(imageToSave, null);
        try {
            ImageIO.write(bufferedImg, format, fileToSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void viewOriginalImage() {
        ImageView.setImage(originalImage);
    }

    @FXML
    public void viewModifiedImage() {
        if (modifiedImage == null) {
            return;
        }
        ImageView.setImage(modifiedImage);
    }

    public void negativeFilter() {
        try {
            Image imageToNegative = ImageView.getImage();
            BufferedImage bufferedImageToNegative = SwingFXUtils.fromFXImage(imageToNegative, null);
            int imageNegativeWidth = bufferedImageToNegative.getWidth();
            int imageNegativeHeight = bufferedImageToNegative.getHeight();
            for (int x=0;x<imageNegativeWidth;x++) {
                for (int y=0;y<imageNegativeHeight;y++){
                    int rgbBuffered = bufferedImageToNegative.getRGB(x, y);
                    Color pixelColor = new Color(rgbBuffered);
                    int r = 255 - pixelColor.getRed();
                    int g = 255 - pixelColor.getGreen();
                    int b = 255 - pixelColor.getBlue();
                    Color colorForPixel = new Color(r, g, b);
                    bufferedImageToNegative.setRGB(x, y, colorForPixel.getRGB());
                }
            }
            Image negativeImage = SwingFXUtils.toFXImage(bufferedImageToNegative, null);
            ImageView.setImage(negativeImage);

            modifiedImage = ImageView.getImage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
