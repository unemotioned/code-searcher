package com.mcs.modelsearcher;

import com.mcs.modelsearcher.file.controller.FileController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

public class MainController {
    @FXML
    private Label filePathLabel;

    @Setter
    FileController fileController;

    @Setter
    private Stage fileChooserStage;

    @FXML
    public void initialize() {
    }

    public void refreshFilePathLabel() {
        if (fileController != null && fileController.getFilePath() != null) {
            filePathLabel.setText(fileController.getFilePath());
        } else {
            filePathLabel.setText("No file selected");
        }
    }

    @FXML
    protected void onSelFileClick() {
        String newPath = fileController.selFileBtnClick(fileChooserStage);
        filePathLabel.setText(newPath);
    }
}
