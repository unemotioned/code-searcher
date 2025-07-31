package com.mcs.modelsearcher.file.controller;

import com.mcs.modelsearcher.file.model.service.FileService;
import com.mcs.modelsearcher.file.model.vo.FilePath;
import com.mcs.modelsearcher.hash.controller.HashController;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileController {
    FilePath filePath;
    FileService fServ;

    public FileController() {
        filePath = new FilePath();
        fServ = new FileService();
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
            new HashController().performHash();
        }
    }

    public String selectPath() {
        return fServ.selectPath();
    }

    private void delInvalidPath() {
        int delPathResult = fServ.delInvalidPath();
        if (delPathResult == 1) {
            System.out.println("fCon.delInvalidPath: success");
        } else {
            System.out.println("fCon.delInvalidPath: fail");
        }
    }

    private String chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file.getAbsolutePath();
        } else {
            System.out.println("fCon.chooseFile: File not selected");
            return null;
        }
    }

    private void insertPath(String path) {
        int insertPath = fServ.insertPath(path);

        if (insertPath == 1) {
            System.out.println("fCon.insertPath: success");
        } else {
            System.out.println("fCon.insertPath: fail");
        }
    }

    public String selFileBtnClick(Stage stage) {
        String path = chooseFile(stage);

        if (path != null) {
            delInvalidPath();
            insertPath(path);
            this.filePath.setFilePath(path);
            new HashController().performHash();
            return path;
        } else {
            return "No file selected";
        }
    }

    public String getFilePath() {
        return filePath.getFilePath();
    }
}
