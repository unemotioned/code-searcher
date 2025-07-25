package com.mcs.modelsearcher.controller;

import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.file.controller.FileController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.Date;

public class MainViewController {
    // @formatter:off
    @FXML private Label filePathLabel;
    @FXML public TextField partCodeInput;

    @FXML private TableView<Excel> excelData;
    @FXML private TableColumn<Excel, String> insertNo;
    @FXML private TableColumn<Excel, String> partCode;
    @FXML private TableColumn<Excel, String> rev;
    @FXML private TableColumn<Excel, String> apply1;
    @FXML private TableColumn<Excel, String> apply2;
    @FXML private TableColumn<Excel, Date> blueprintDate;
    @FXML private TableColumn<Excel, Boolean> clientBlueprint;
    @FXML private TableColumn<Excel, Boolean> scan;
    @FXML private TableColumn<Excel, Boolean> selfBlueprint;
    @FXML private TableColumn<Excel, String> category;
    @FXML private TableColumn<Excel, String> name;
    @FXML private TableColumn<Excel, String> spec;
    @FXML private TableColumn<Excel, String> maker;
    @FXML private TableColumn<Excel, String> vendor;
    @FXML private TableColumn<Excel, Integer> unitPrice;
    @FXML private TableColumn<Excel, Integer> mgmtCost;
    @FXML private TableColumn<Excel, Integer> estPrice;
    @FXML private TableColumn<Excel, Integer> refPrice;
    @FXML private TableColumn<Excel, String> note;

    @Setter FileController fileController;
    @Setter private Stage fileChooserStage;
    // @formatter:on

    @FXML
    public void initialize() {
        insertNo.setCellValueFactory(new PropertyValueFactory<>("insertNo"));
        partCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        rev.setCellValueFactory(new PropertyValueFactory<>("rev"));
        apply1.setCellValueFactory(new PropertyValueFactory<>("apply1"));
        apply2.setCellValueFactory(new PropertyValueFactory<>("apply2"));
        blueprintDate.setCellValueFactory(new PropertyValueFactory<>("blueprintDate"));
        clientBlueprint.setCellValueFactory(new PropertyValueFactory<>("blueprint"));
        scan.setCellValueFactory(new PropertyValueFactory<>("scan"));
        selfBlueprint.setCellValueFactory(new PropertyValueFactory<>("selfBlueprint"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        spec.setCellValueFactory(new PropertyValueFactory<>("spec"));
        maker.setCellValueFactory(new PropertyValueFactory<>("maker"));
        vendor.setCellValueFactory(new PropertyValueFactory<>("vendor"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        mgmtCost.setCellValueFactory(new PropertyValueFactory<>("mgmtCost"));
        estPrice.setCellValueFactory(new PropertyValueFactory<>("estPrice"));
        refPrice.setCellValueFactory(new PropertyValueFactory<>("refPrice"));
        note.setCellValueFactory(new PropertyValueFactory<>("note"));

        // focus on the text field on start
        Platform.runLater(() -> partCodeInput.requestFocus());

        // call searchPartCode() on every input
        partCodeInput.textProperty().addListener((observable, oldValue, newValue) -> searchPartCode());
    }

    public void searchPartCode() {
        String keyword = partCodeInput.getText();
        System.out.println("keyword: " + keyword);

        // show it on the tableView
        ObservableList<Excel> excel = FXCollections.observableArrayList();
        excelData.setItems(excel);
    }

    public void refreshFilePathLabel() {
        if (fileController != null && fileController.getFilePath() != null) {
            filePathLabel.setText(fileController.getFilePath());
        } else {
            filePathLabel.setText("No file selected");
        }
    }

    @FXML
    protected void onSelFileClick() {
        String newPath = fileController.selFileBtnClick(fileChooserStage);
        filePathLabel.setText(newPath);
    }
}
