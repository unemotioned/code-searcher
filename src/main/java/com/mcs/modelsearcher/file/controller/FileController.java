package com.mcs.modelsearcher.file.controller;

import com.mcs.modelsearcher.file.model.service.FileService;
import com.mcs.modelsearcher.file.model.vo.FilePath;
import com.mcs.modelsearcher.hash.controller.HashController;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileController {
    FilePath filePath;
    FileService service;

    public FileController() {
        filePath = new FilePath();
        service = new FileService();
    }

    public void openFile(Stage stage) {
        String prevPath = selPath();

        if (prevPath != null) {
            File file = new File(prevPath);
            if (file.exists()) {
                filePath.setFilePath(prevPath);

                // pass current instance of FileController object
                HashController hCon = new HashController(this);
                hCon.performHash();
            } else {
                delInvalidPath();

                String newPath = chooseFile(stage);
                insertPath(newPath);
                filePath.setFilePath(newPath);
            }
        }
    }

    public String selPath() {
        return service.selPath();
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
