package com.mcs.codesearcher;

import com.mcs.codesearcher.common.DatabaseInitializer;
import com.mcs.codesearcher.controller.MainController;
import com.mcs.codesearcher.file.controller.FileController;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CodeSearcher extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    new DatabaseInitializer();

    FileController fCon = new FileController();
    fCon.openFile(stage);

    FXMLLoader fxmlLoader = new FXMLLoader(CodeSearcher.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    scene.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/com/mcs/codesearcher/style/main-view.css"))
            .toExternalForm());

    // Get the controller and pass the stage
    MainController mCon = fxmlLoader.getController();
    mCon.setFileChooserStage(stage);
    mCon.setFileController(fCon);
    mCon.refreshFilePathLabel();

    stage.setTitle("Component code searcher");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void stop() {
    System.out.println("CodeSearcher.stop(): application closing ...");
    Platform.exit(); // exit gracefully
  }
}
