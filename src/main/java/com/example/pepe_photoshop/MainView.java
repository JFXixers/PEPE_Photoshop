package com.example.pepe_photoshop;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class MainView {

    @FXML
    private Pane ImagePane;

    @FXML
    public VBox SideBar;
    @FXML
    public MenuBar MenuBar;

    @FXML
    public Menu MenuFile;
    @FXML
    public ImageView LoadImageIcon;
    @FXML
    public ImageView SaveImageIcon;

    @FXML
    public Menu MenuFilters;
    @FXML
    public ImageView FilterNegativeIcon;
    @FXML
    public ImageView FilterPixelizerIcon;

    @FXML
    public Menu MenuAbout;
    @FXML
    public Menu MenuExit;
    @FXML
    public RadioButton OriginalImageRadio;
    @FXML
    public ImageView OriginalImage;
    @FXML
    public RadioButton ModifiedImageRadio;
    @FXML
    public ImageView ModifiedImage;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private ImageView ImageView;

    @FXML
    private ToggleButton DarkModeToggle;
    @FXML
    private ImageView DarkModeIcon;

    public static Image originalImage;
    public static Image modifiedImage;

    public void loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.bmp", "*.png"));

        Image image = new Image(fileChooser.showOpenDialog(AnchorPane.getScene().getWindow()).toURI().toString());

        ImageView.setImage(image);
        originalImage = ImageView.getImage();
        modifiedImage = null;

        ImageView.fitWidthProperty().bind(ImagePane.widthProperty());
        ImageView.fitHeightProperty().bind(ImagePane.heightProperty());
    }

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

    // Due to JavaFX constraints this function can only be called on showing a menu.
    // Therefore, the Exit "button" needs a sub menu.
    // But since it is called on showing the menu, then the user will never get to see it.
    // The about button is made the same way. If you find a better one you're free to improve! ☕
    public void closeApplication() {
        Stage stage = (Stage) AnchorPane.getScene().getWindow();
        stage.close();
    }

    public void aboutApplication() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("made by JFixers,\nthanks to ☕ and sleepless nights");
        alert.show();
    }

    public void viewOriginalImage() {
        ImageView.setImage(originalImage);
    }

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
    public void makeSliderForPixelize(){
        PauseTransition delay = new PauseTransition(Duration.millis(50));
        Slider sliderForPixelize = new Slider();
        sliderForPixelize.setMin(0);
        sliderForPixelize.setMax(100);
        sliderForPixelize.setValue(20);
        sliderForPixelize.setBlockIncrement(1);
        sliderForPixelize.setMajorTickUnit(1);
        sliderForPixelize.setMinorTickCount(0);
        sliderForPixelize.setSnapToTicks(true);
        sliderForPixelize.valueProperty().addListener((obs, oldVal, newVal) -> {
            delay.setOnFinished(e -> pixelizeFilter(newVal.intValue()));
            delay.playFromStart();
        });

        SideBar.getChildren().add(new Label("Pixelizer:"));
        SideBar.getChildren().add(sliderForPixelize);
    }

    public void pixelizeFilter(int factor){
        try{
            Image imageToPixelize = ImageView.getImage();
            BufferedImage bufferedImageToPixelize = SwingFXUtils.fromFXImage(imageToPixelize, null);
            int imagePixelizeWidth = bufferedImageToPixelize.getWidth();
            int imagePixelizeHeight = bufferedImageToPixelize.getHeight();
            for (int x=0;x<imagePixelizeWidth;x+=factor*2){
                for (int y=0;y<imagePixelizeHeight;y+=factor*2){
                    for (int a=x-factor; a<x+factor;a++){
                        for (int b=y-factor;b<y+factor;b++){
                            int getA = Math.min(bufferedImageToPixelize.getWidth()-1, Math.max(0, a));
                            int getB = Math.min(bufferedImageToPixelize.getHeight()-1, Math.max(0, b));
                            Color c = new Color(bufferedImageToPixelize.getRGB(x, y));
                            bufferedImageToPixelize.setRGB(getA, getB, c.getRGB());
                        }
                    }

                }
            }
            Image pixelizedImage = SwingFXUtils.toFXImage(bufferedImageToPixelize, null);
            ImageView.setImage(pixelizedImage);

            modifiedImage = ImageView.getImage();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage makeColoredImage(){
        BufferedImage bImage = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
        for (int x=0;x<bImage.getWidth();x++){
            for (int y=0;y<bImage.getHeight();y++){
                bImage.setRGB(x, y, (new Color(x%70, y%200, (x+y)%100).getRGB()));
            }
        }
        return bImage;
    }

    public void generateImage(){
        BufferedImage bimg = makeColoredImage();
        Image generatedImage = SwingFXUtils.toFXImage(bimg, null);
        ImageView.setImage(generatedImage);
        ImageView.fitWidthProperty().bind(ImagePane.widthProperty());
        ImageView.fitHeightProperty().bind(ImagePane.heightProperty());

    }

    public void filterThreshold(){
        BufferedImage bufferedImageToThreshold = SwingFXUtils.fromFXImage(ImageView.getImage(), null);
        int black = Color.BLACK.getRGB();
        int white = Color.WHITE.getRGB();
        int threshold = 128;
        for (int x = 0;x<bufferedImageToThreshold.getWidth();x++){
            for (int y=0;y<bufferedImageToThreshold.getHeight();y++){
                int rgb = bufferedImageToThreshold.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int gray = (int)(0.299*r + 0.587*g + 0.114*b);
                if (gray < threshold){
                    bufferedImageToThreshold.setRGB(x, y, black);
                } else{
                    bufferedImageToThreshold.setRGB(x, y, white);
                }
            }
        }
        Image filteredThresholdImage = SwingFXUtils.toFXImage(bufferedImageToThreshold, null);
        ImageView.setImage(filteredThresholdImage);

        modifiedImage = ImageView.getImage();
    }

    public static final Map<String, Paint> LightModeColors = Map.ofEntries(
            Map.entry("imageBackground", Paint.valueOf("#FFF5F2")),
            Map.entry("sidebarBackground", Paint.valueOf("#EAE5E4")),
            Map.entry("menuBackground", Paint.valueOf("#B9B7B7"))
    );

    public static final Map<String, Paint> DarkModeColors = Map.ofEntries(
            Map.entry("imageBackground", Paint.valueOf("#4e5b67")),
            Map.entry("sidebarBackground", Paint.valueOf("#384754")),
            Map.entry("menuBackground", Paint.valueOf("#1f2d3b"))
    );


    @FXML
    public void toggleDarkMode() {
        if (DarkModeToggle.isSelected()) {
            DarkModeToggle.setText("Dark");
            DarkModeIcon.setImage(new Image(getClass().getResource("/images/DarkMode.png").toExternalForm()));

            ImagePane.setBackground(new Background(new BackgroundFill(DarkModeColors.get("imageBackground"), new CornerRadii(0), Insets.EMPTY)));
            SideBar.setBackground(new Background(new BackgroundFill(DarkModeColors.get("sidebarBackground"), new CornerRadii(0), Insets.EMPTY)));
            MenuBar.setBackground(new Background(new BackgroundFill(DarkModeColors.get("menuBackground"), new CornerRadii(0), Insets.EMPTY)));
            DarkModeToggle.setStyle(
                    "-fx-background-color: #" + DarkModeColors.get("menuBackground").toString().substring(2, 8) + ";" +
                            "-fx-background-radius: 0;"
            );
            DarkModeToggle.setTextFill(Paint.valueOf("#ffffff"));
            MenuFile.setStyle("-fx-font-size: 18px;-fx-text-base-color: #ffffff;");
            MenuFilters.setStyle("-fx-font-size: 18px;-fx-text-base-color: #ffffff;");
            MenuAbout.setStyle("-fx-font-size: 18px;-fx-text-base-color: #ffffff;");
            MenuExit.setStyle("-fx-font-size: 18px;-fx-text-base-color: #ffffff;");

            OriginalImageRadio.setStyle("-fx-text-fill: #ffffff;-fx-padding: 2 0 2 0;");
            OriginalImage.setImage(new Image(getClass().getResource("/images/OriginalLight.png").toExternalForm()));
            ModifiedImageRadio.setStyle("-fx-text-fill: #ffffff;-fx-padding: 2 0 2 0;");
            ModifiedImage.setImage(new Image(getClass().getResource("/images/ModifiedLight.png").toExternalForm()));

            MenuFile.setOnShowing(e -> {
                MenuFile.getItems().forEach(item -> {
                    item.getParentPopup().setStyle("-fx-background-color: #" + DarkModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
                });
            });
            MenuFilters.setOnShowing(e -> {
                MenuFilters.getItems().forEach(item -> {
                    item.getParentPopup().setStyle("-fx-background-color: #" + DarkModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
                });
            });
            MenuAbout.setOnShowing(e -> {
                MenuAbout.getItems().forEach(item -> {
                    item.getParentPopup().setStyle("-fx-background-color: #" + DarkModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
                });
            });
            MenuExit.setOnShowing(e -> {
                MenuExit.getItems().forEach(item -> {
                    item.getParentPopup().setStyle("-fx-background-color: #" + DarkModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
                });
            });

            LoadImageIcon.setImage(new Image(getClass().getResource("/images/LoadFileLight.png").toExternalForm()));
            SaveImageIcon.setImage(new Image(getClass().getResource("/images/SaveFileLight.png").toExternalForm()));

            FilterNegativeIcon.setImage(new Image(getClass().getResource("/images/NegativeLight.png").toExternalForm()));
            FilterPixelizerIcon.setImage(new Image(getClass().getResource("/images/PixelizedLight.png").toExternalForm()));

        } else {
            DarkModeToggle.setText("Light");
            DarkModeIcon.setImage(new Image(getClass().getResource("/images/LightMode.png").toExternalForm()));

            ImagePane.setBackground(new Background(new BackgroundFill(LightModeColors.get("imageBackground"), new CornerRadii(0), Insets.EMPTY)));
            SideBar.setBackground(new Background(new BackgroundFill(LightModeColors.get("sidebarBackground"), new CornerRadii(0), Insets.EMPTY)));
            MenuBar.setBackground(new Background(new BackgroundFill(LightModeColors.get("menuBackground"), new CornerRadii(0), Insets.EMPTY)));
            DarkModeToggle.setStyle(
                    "-fx-background-color: #" + LightModeColors.get("menuBackground").toString().substring(2, 8) + ";" +
                            "-fx-background-radius: 0;"
            );
            DarkModeToggle.setTextFill(Paint.valueOf("#000000"));
            MenuFile.setStyle("-fx-font-size: 18px;-fx-text-base-color: #000000;");
            MenuFilters.setStyle("-fx-font-size: 18px;-fx-text-base-color: #000000;");
            MenuAbout.setStyle("-fx-font-size: 18px;-fx-text-base-color: #000000;");
            MenuExit.setStyle("-fx-font-size: 18px;-fx-text-base-color: #000000;");

            OriginalImageRadio.setStyle("-fx-text-fill: #000000;-fx-padding:  2 0 2 0;");
            OriginalImage.setImage(new Image(getClass().getResource("/images/Original.png").toExternalForm()));
            ModifiedImageRadio.setStyle("-fx-text-fill: #000000;-fx-padding:  2 0 2 0;");
            ModifiedImage.setImage(new Image(getClass().getResource("/images/Modified.png").toExternalForm()));

            MenuFile.setOnShowing(e -> {
                MenuFile.getItems().forEach(item -> {
                    item.getParentPopup().setStyle("-fx-background-color: #" + LightModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
                });
            });
            MenuFilters.setOnShowing(e -> {
                MenuFilters.getItems().forEach(item -> {
                    item.getParentPopup().setStyle("-fx-background-color: #" + LightModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
                });
            });
            MenuAbout.setOnShowing(e -> {
                MenuAbout.getItems().forEach(item -> {
                    item.getParentPopup().setStyle("-fx-background-color: #" + LightModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
                });
            });
            MenuExit.setOnShowing(e -> {
                MenuExit.getItems().forEach(item -> {
                    item.getParentPopup().setStyle("-fx-background-color: #" + LightModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
                });
            });

            LoadImageIcon.setImage(new Image(getClass().getResource("/images/LoadFile.png").toExternalForm()));
            SaveImageIcon.setImage(new Image(getClass().getResource("/images/SaveFile.png").toExternalForm()));

            FilterNegativeIcon.setImage(new Image(getClass().getResource("/images/Negative.png").toExternalForm()));
            FilterPixelizerIcon.setImage(new Image(getClass().getResource("/images/Pixelized.png").toExternalForm()));
        }
    }


    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // Make the title bar draggable
        MenuBar.setOnMousePressed((MouseEvent event) -> {
            Stage stage = (Stage) MenuBar.getScene().getWindow();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        MenuBar.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) MenuBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });




        MenuFile.setOnShowing(e -> {
            MenuFile.getItems().forEach(item -> {
                item.getParentPopup().setStyle("-fx-background-color: #" + LightModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
            });
        });
        MenuFilters.setOnShowing(e -> {
            MenuFilters.getItems().forEach(item -> {
                item.getParentPopup().setStyle("-fx-background-color: #" + LightModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
            });
        });
        MenuAbout.setOnShowing(e -> {
            MenuAbout.getItems().forEach(item -> {
                item.getParentPopup().setStyle("-fx-background-color: #" + LightModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
            });
        });
        MenuExit.setOnShowing(e -> {
            MenuExit.getItems().forEach(item -> {
                item.getParentPopup().setStyle("-fx-background-color: #" + LightModeColors.get("sidebarBackground").toString().substring(2, 8) + ";");
            });
        });
    }
}
