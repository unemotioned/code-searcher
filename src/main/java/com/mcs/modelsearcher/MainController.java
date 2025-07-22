package com.mcs.modelsearcher;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label filePath;

    @FXML
    public void initialize() {
    }

    @FXML
    protected void onSelectFileClick() {
        filePath.setText("Select file button clicked!");
    }
}
