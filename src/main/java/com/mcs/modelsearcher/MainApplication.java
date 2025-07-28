package com.mcs.modelsearcher;

import com.mcs.modelsearcher.controller.MainViewController;
import com.mcs.modelsearcher.file.controller.FileController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FileController fileController = new FileController();
        fileController.openFile(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/mcs/modelsearcher/style/main-view.css")).toExternalForm());

        // Get the controller and pass the stage
        MainViewController con = fxmlLoader.getController();
        con.setFileChooserStage(stage);
        con.setFileController(fileController);
        con.refreshFilePathLabel();

        stage.setTitle("Component code searcher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        System.out.println("MainApplication.stop(): application closing ...");
        // exit gracefully
        Platform.exit();
    }
}