package com.mcs.modelsearcher.file.controller;

import com.mcs.modelsearcher.file.model.service.FileService;
import com.mcs.modelsearcher.file.model.vo.FilePath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileController {
    FilePath path = new FilePath();
    FileService service = new FileService();

    public void openFile(Stage stage) {
        // 1. if invalid path (including first time)
        String newPath = newPath(stage);
        path.setFilePath(newPath);
        System.out.println("newPath: " + path.getFilePath());

        int insertPath = service.insertPath(newPath);

        if (insertPath == 0) {
            System.out.println("insertPath: fail");
        } else {
            System.out.println("insertPath: success");
        }

        // 2. read from db about file path
    }

    /**
     * Opens file explorer to choose Excel file
     */
    public String newPath(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
        File file = fileChooser.showOpenDialog(stage);
        return file != null ? file.getAbsolutePath() : null;
    }
}
