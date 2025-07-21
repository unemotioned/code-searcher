package com.mcs.modelsearcher;

import com.mcs.modelsearcher.file.controller.FileController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FileController fileController = new FileController();
        fileController.openFile(stage);

        // if choosing file fails
        System.out.println("No valid file selected");
        Platform.exit(); // gracefully exit
        System.exit(0); // fully exit

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        stage.setTitle("Model number searcher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}