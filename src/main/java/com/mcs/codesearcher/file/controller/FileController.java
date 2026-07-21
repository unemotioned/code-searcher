package com.mcs.codesearcher.file.controller;

import com.mcs.codesearcher.file.model.service.FileService;
import com.mcs.codesearcher.file.model.vo.FilePath;
import com.mcs.codesearcher.hash.controller.HashController;
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
        int result = fServ.delInvalidPath();
        if (result == 1) {
            System.out.println("fCon.delInvalidPath: success");
        } else {
            System.out.println("fCon.delInvalidPath: fail");
        }
    }

    private String chooseFile(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));

        File f = fc.showOpenDialog(stage);
        if (f != null) {
            return f.getAbsolutePath();
        } else {
            System.out.println("fCon.chooseFile: File not selected");
            return null;
        }
    }

    private void insertPath(String path) {
        int result = fServ.insertPath(path);
        if (result == 1) {
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
