package com.mcs.modelsearcher;

import com.mcs.modelsearcher.file.controller.FileController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FileController fileController = new FileController();
        fileController.openFile(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Get the controller and pass the stage
        MainController controller = fxmlLoader.getController();
        controller.setFileChooserStage(stage);

        stage.setTitle("Model number searcher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}