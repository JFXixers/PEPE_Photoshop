package com.example.pepe_photoshop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle(null);
        stage.setScene(scene);
        stage.setMinHeight(300.0);
        stage.setMinWidth(400.0);
        stage.show();
    }
/*
    public static void main(String[] args) {
        launch(args);
    }
*/
}