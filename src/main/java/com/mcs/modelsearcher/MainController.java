package com.mcs.modelsearcher;

import com.mcs.modelsearcher.file.controller.FileController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

public class MainController {
    @FXML
    private Label filePathLabel;

    FileController fileCon = new FileController();

    @Setter
    private Stage fileChooserStage;

    @FXML
    public void initialize() {
        fileCon.selPath();
        filePathLabel.setText(fileCon.getFilePath());
    }

    @FXML
    protected void onSelFileClick() {
        String newPath = fileCon.selFileBtnClick(fileChooserStage);
        filePathLabel.setText(newPath);
    }
}
