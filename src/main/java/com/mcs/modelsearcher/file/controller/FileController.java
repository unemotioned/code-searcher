package com.mcs.modelsearcher.file.controller;

import com.mcs.modelsearcher.file.model.service.FileService;
import com.mcs.modelsearcher.file.model.vo.FilePath;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileController {
    FilePath filePath = new FilePath();
    FileService service = new FileService();

    public void openFile(Stage stage) {
        boolean isPathValid = selPath();

        if (!isPathValid) {
            String newPath = chooseFile(stage);
            if (newPath != null) {
                insertPath(newPath);
            }
        } else {
            // TODO: hash each sheets and compare
            System.out.println("Hash each sheets.");
        }
    }

    public boolean selPath() {
        boolean isPathValid = false;
        String prevPath = service.selPath();

        if (prevPath != null) {
            File file = new File(prevPath);

            if (file.exists()) {
                filePath.setFilePath(prevPath);

                isPathValid = true;
            } else {
                delInvalidPath();
            }
        }
        return isPathValid;
    }

    private void delInvalidPath() {
        int delPathResult = service.delInvalidPath();
        System.out.println("Number of deleted path: " + delPathResult);
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

    public String getFilePath() {
        return filePath.getFilePath();
    }

    public String selFileBtnClick(Stage stage) {
        delInvalidPath();
        String path = chooseFile(stage);
        if (path != null) {
            filePath.setFilePath(path);
            insertPath(path);
        }
        return path;
    }
}
