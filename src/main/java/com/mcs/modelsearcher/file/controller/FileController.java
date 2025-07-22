package com.mcs.modelsearcher.file.controller;

import com.mcs.modelsearcher.file.model.service.FileService;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileController {
    FileService service = new FileService();

    public void openFile(Stage stage) {
        boolean isPathValid = selectPath();

        if (!isPathValid) {
            String newPath = chooseFile(stage);
            insertPath(newPath);
        } else {
            // TODO: hash each sheets and compare
            System.out.println("Hash each sheets.");
        }
    }

    public boolean selectPath() {
        boolean isPathValid = false;
        String prevPath = service.selectPath();

        if (prevPath != null) {
            File file = new File(prevPath);
            if (file.exists()) {
                isPathValid = true;
            } else {
                deleteInvalidPath();
            }
        }
        return isPathValid;
    }

    private void deleteInvalidPath() {
        int deletePathResult = service.deleteInvalidPath();
        System.out.println("Path deleted: " + deletePathResult);
    }

    public String chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file.getAbsolutePath();
        } else {
            System.out.println("File not selected");
            Platform.exit(); // gracefully exit
            System.exit(0); // fully exit

            return null;
        }
    }

    public void insertPath(String path) {
        int insertPath = service.insertPath(path);

        if (insertPath == 0) {
            System.out.println("insertPath: fail");
        } else {
            System.out.println("insertPath: success");
        }
    }
}
