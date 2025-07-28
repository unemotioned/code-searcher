package com.mcs.modelsearcher.controller;

import com.mcs.modelsearcher.excel.controller.SearchController;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.file.controller.FileController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class MainViewController {
    // @formatter:off
    @FXML private Label filePathLabel;

    @FXML public TextField partCodeInput;
    @FXML public TextField apply1Input;
    @FXML public TextField apply2Input;
    @FXML public TextField categoryInput;
    @FXML public TextField nameInput;
    @FXML public TextField specInput;
    @FXML public TextField vendorInput;
    @FXML public TextField noteInput;

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

    @FXML private TextField lastFocusedTextField;
    // @formatter:on

    SearchController searchCon;

    private List<TextField> textFields;

    public MainViewController() {
        searchCon = new SearchController();
    }

    @FXML
    public void initialize() {
        setTableColumn();

        // focus on the text field on start
        Platform.runLater(() -> partCodeInput.requestFocus());

        // searching
        getTextFieldInput();

        doubleRightClickRow();
        copyCellOnDoubleClick();

        // tab and arrow up/down
        focusControl();
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

    public void getTextFieldInput() {
        // do search on every input
        partCodeInput.textProperty().addListener((observable, oldValue, newValue) -> doSearch());
        apply1Input.textProperty().addListener((observable, oldValue, newValue) -> doSearch());
        apply2Input.textProperty().addListener((observable, oldValue, newValue) -> doSearch());
        categoryInput.textProperty().addListener((observable, oldValue, newValue) -> doSearch());
        nameInput.textProperty().addListener((observable, oldValue, newValue) -> doSearch());
        specInput.textProperty().addListener((observable, oldValue, newValue) -> doSearch());
        vendorInput.textProperty().addListener((observable, oldValue, newValue) -> doSearch());
        noteInput.textProperty().addListener((observable, oldValue, newValue) -> doSearch());
    }

    public void doSearch() {
        HashMap<String, String> userInput = new HashMap<>();
        userInput.put("partCode", partCodeInput.getText().trim().toUpperCase());
        userInput.put("apply1", apply1Input.getText().trim());
        userInput.put("apply2", apply2Input.getText().trim());
        userInput.put("category", categoryInput.getText().trim());
        userInput.put("name", nameInput.getText().trim().toUpperCase());
        userInput.put("spec", specInput.getText().trim().toUpperCase());
        userInput.put("vendor", vendorInput.getText().trim());
        userInput.put("note", noteInput.getText().trim());

        List<Excel> result = searchCon.doSearch(userInput);
        // convert list to observable
        ObservableList<Excel> observableResult = FXCollections.observableArrayList(result);
        excelData.setItems(observableResult);
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

    public void focusControl() {
        // order matters
        textFields = List.of(partCodeInput, apply1Input, apply2Input, categoryInput, nameInput, specInput, vendorInput, noteInput);

        // Handle text field behavior
        for (TextField tf : textFields) {
            tf.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case TAB:
                        event.consume();
                        focusNextTextField(tf, event.isShiftDown());
                        break;
                    case DOWN:
                        event.consume();
                        excelData.requestFocus();
                        break;
                    // do nothing on other key press
                    default:
                        break;
                }
            });

            // Track last focused TextField
            tf.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    lastFocusedTextField = tf;
                }
            });
        }

        // Track if ArrowUp was already pressed on the top row
        final BooleanProperty upPressedAtTopRow = new SimpleBooleanProperty(false);

        excelData.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                TableView.TableViewSelectionModel<?> selectionModel = excelData.getSelectionModel();
                int selectedIndex = selectionModel.getSelectedIndex();

                if (selectedIndex == 0) {
                    if (upPressedAtTopRow.get()) {
                        // Second consecutive UP at top row — shift focus back
                        event.consume();
                        if (lastFocusedTextField != null) {
                            lastFocusedTextField.requestFocus();
                        } else {
                            textFields.getLast().requestFocus();
                        }
                        // reset
                        upPressedAtTopRow.set(false);
                    } else {
                        // First UP at top row — mark but allow normal behavior
                        upPressedAtTopRow.set(true);
                    }
                } else {
                    // reset if not at top
                    upPressedAtTopRow.set(false);
                }
            } else {
                // reset on any other key
                upPressedAtTopRow.set(false);
            }
        });
    }

    private void focusNextTextField(TextField current, boolean reverse) {
        int index = textFields.indexOf(current);
        int nextIndex = reverse ? (index - 1 + textFields.size()) % textFields.size() : (index + 1) % textFields.size();
        textFields.get(nextIndex).requestFocus();
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
}
