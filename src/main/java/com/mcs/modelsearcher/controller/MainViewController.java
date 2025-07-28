package com.mcs.modelsearcher.controller;

import com.mcs.modelsearcher.excel.controller.SearchController;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.file.controller.FileController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainViewController {
    // @formatter:off
    @FXML private Label filePathLabel;

    @FXML public TextField uniSearch;

    @FXML private TableView<Excel> excelData;
    @FXML private TableColumn<Excel, String> insertNo;
    @FXML private TableColumn<Excel, String> partCode;
    @FXML private TableColumn<Excel, String> rev;
    @FXML private TableColumn<Excel, String> apply1;
    @FXML private TableColumn<Excel, String> apply2;
    @FXML private TableColumn<Excel, Date> blueprintDate;
    @FXML private TableColumn<Excel, String> clientBlueprint;
    @FXML private TableColumn<Excel, String> scan;
    @FXML private TableColumn<Excel, String> selfBlueprint;
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

    SearchController searchCon;

    public MainViewController() {
        searchCon = new SearchController();
    }

    @FXML
    public void initialize() {
        setTableColumn();

        Platform.runLater(() -> uniSearch.requestFocus());
        uniSearch();

        doubleRightClickRow();
        copyCellOnDoubleClick();
    }

    private void doubleRightClickRow() {
        excelData.setOnMouseClicked(event -> {
            if (event.isSecondaryButtonDown()) {
                Excel selectedItem = excelData.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    System.out.println("Right-clicked: " + selectedItem.getPartCode());
                }
            }
        });
    }

    private void copyCellOnDoubleClick() {
        excelData.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TablePosition<?, ?> position = excelData.getSelectionModel().getSelectedCells().getFirst();
                int row = position.getRow();
                int column = position.getColumn();

                Object cellData = excelData.getColumns().get(column).getCellData(row);

                System.out.println("Clicked Cell Value: " + cellData);

                if (cellData != null) {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(cellData.toString());
                    clipboard.setContent(content);
                }
            }
        });
    }

    public void uniSearch() {
        uniSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            String input = uniSearch.getText().trim();
            StringTokenizer tokenizer = new StringTokenizer(input);

            ArrayList<String> keywordList = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                keywordList.add(tokenizer.nextToken());
            }

            List<Excel> result = searchCon.uniSearch(keywordList);

            ObservableList<Excel> observableResult = FXCollections.observableArrayList(result);
            excelData.setItems(observableResult);
        });
    }

    @FXML
    protected void onSelFileClick() {
        String newPath = fileController.selFileBtnClick(fileChooserStage);
        filePathLabel.setText(newPath);
    }

    @FXML
    public void onInsertBtnClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mcs/modelsearcher/insert-popup.fxml"));
            Parent root = loader.load();
            InsertPopupController insertCon = loader.getController();

            Stage stage = new Stage();
            insertCon.setStage(stage);

            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // disable main-view
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening insert popup: " + e.getMessage());
        }
    }

    public void setTableColumn() {
        insertNo.setCellValueFactory(new PropertyValueFactory<>("insertNo"));
        partCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        rev.setCellValueFactory(new PropertyValueFactory<>("rev"));
        apply1.setCellValueFactory(new PropertyValueFactory<>("apply1"));
        apply2.setCellValueFactory(new PropertyValueFactory<>("apply2"));
        blueprintDate.setCellValueFactory(new PropertyValueFactory<>("blueprintDate"));
        clientBlueprint.setCellValueFactory(new PropertyValueFactory<>("clientBlueprint"));
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
    }

    public void refreshFilePathLabel() {
        if (fileController != null && fileController.getFilePath() != null) {
            filePathLabel.setText(fileController.getFilePath());
        } else {
            filePathLabel.setText("No file selected");
        }
    }
}
