package com.mcs.modelsearcher.file.controller;

import com.mcs.modelsearcher.file.model.service.FileService;
import com.mcs.modelsearcher.file.model.vo.FilePath;
import com.mcs.modelsearcher.hash.controller.HashController;
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
        String path = selectPath();

        if (path == null || !new File(path).exists()) {
            if (path != null) {
                delInvalidPath();
            }

            path = chooseFile(stage);
            if (path != null) {
                insertPath(path);
            }
        }

        if (path != null) {
            filePath.setFilePath(path);
            new HashController(this).performHash();
        }
    }

    public String selectPath() {
        return service.selectPath();
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
            System.out.println("FileController.chooseFile(): File not selected");
            return null;
        }
    }

    public void insertPath(String path) {
        int insertPath = service.insertPath(path);

        if (insertPath == 1) {
            System.out.println("FileController.insertPath(): success");
        } else {
            System.out.println("FileController.insertPath(): fail");
        }
    }

    public String getFilePath() {
        return filePath.getFilePath();
    }

    public String selFileBtnClick(Stage stage) {
        String path = chooseFile(stage);
        if (path != null) {
            delInvalidPath();
            insertPath(path);
            filePath.setFilePath(path);
            return path;
        } else {
            return "No file selected";
        }
    }
}
