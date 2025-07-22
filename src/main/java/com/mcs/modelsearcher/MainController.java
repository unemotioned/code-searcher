package com.mcs.modelsearcher;

import com.mcs.modelsearcher.file.controller.FileController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label filePath;

    FileController fileController = new FileController();

    @FXML
    public void initialize() {
        fileController.selectPath();
        filePath.setText(fileController.getFilePath());
    }

    @FXML
    protected void onSelectFileClick() {
        fileController.selectFileButtonClicked();
    }
}
