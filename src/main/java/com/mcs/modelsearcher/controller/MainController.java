package com.mcs.modelsearcher.controller;

import com.mcs.modelsearcher.excel.controller.ExcelController;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.file.controller.FileController;
import com.mcs.modelsearcher.hash.controller.HashController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.*;

public class MainController {
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

    ExcelController eCon;
    HashController hCon;

    public MainController() {
        eCon = new ExcelController();
        hCon = new HashController();
    }

    @FXML
    public void initialize() {
        setTableColumn();

        Platform.runLater(() -> uniSearch.requestFocus());
        uniSearch();

        recordContextMenu();
        copyCellOnDoubleClick();
    }

    public void recordContextMenu() {
        excelData.setRowFactory(tv -> {
            TableRow<Excel> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("수정");
            editItem.setOnAction(e -> {
                Excel item = row.getItem();
                editPopup(item);
            });

            MenuItem deleteItem = new MenuItem("삭제");
            deleteItem.setOnAction(e -> {
                Excel item = row.getItem();
                String insertNo = item.getInsertNo();

                boolean isDeleteConform = showDeleteConfirmation(item);
                if (isDeleteConform) {
                    eCon.deleteFromExcel(insertNo);
                    eCon.deleteFromDb(insertNo);
                }
            });

            contextMenu.getItems().addAll(editItem, deleteItem);

            row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));

            return row;
        });
    }

    private void editPopup(Excel item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mcs/modelsearcher/edit-view.fxml"));
            Parent root = loader.load();
            EditViewController editCon = loader.getController();

            Stage editPopupStage = new Stage();
            editCon.setStage(editPopupStage);
            editCon.initialize(item);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/mcs/modelsearcher/style/main-view.css")).toExternalForm());

            editPopupStage.setScene(scene);
            editPopupStage.initModality(Modality.APPLICATION_MODAL); // disable main-view
            editPopupStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening edit popup: " + e.getMessage());
        }

    }

    private boolean showDeleteConfirmation(Excel item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("삭제");
        alert.setHeaderText("삭제 하시겠습니까?");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("등록 No: "), new Label(item.getInsertNo()));
        grid.addRow(1, new Label("부품 코드: "), new Label(item.getPartCode()));
        grid.addRow(2, new Label("적용1: "), new Label(item.getApply1()));
        grid.addRow(3, new Label("Name: "), new Label(item.getName()));
        grid.addRow(4, new Label("규격: "), new Label(item.getSpec()));
        grid.addRow(5, new Label("Maker: "), new Label(item.getMaker()));
        grid.addRow(6, new Label("구입처: "), new Label(item.getVendor()));

        alert.getDialogPane().setContent(grid);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
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

    private void uniSearch() {
        uniSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            Task<List<Excel>> searchTask = getListTask();

            searchTask.setOnSucceeded(event -> {
                List<Excel> results = searchTask.getValue();
                ObservableList<Excel> observableResult = FXCollections.observableArrayList(results);
                excelData.setItems(observableResult);
            });

            searchTask.setOnFailed(event -> {
                System.out.println("uniSearch in Thread failed");
            });

            new Thread(searchTask).start();
        });
    }

    private Task<List<Excel>> getListTask() {
        String input = uniSearch.getText().trim();
        StringTokenizer tokenizer = new StringTokenizer(input);

        ArrayList<String> keywordList = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            keywordList.add(tokenizer.nextToken());
        }

        return new Task<>() {
            @Override
            protected List<Excel> call() {
                return eCon.uniSearch(keywordList);
            }
        };
    }

    @FXML
    protected void onFileSelect() {
        String newPath = fileController.selFileBtnClick(fileChooserStage);
        filePathLabel.setText(newPath);
    }

    @FXML
    public void onReload() {
        // To force to update DATA and HIERARCHY table when .performHash()
        hCon.fakeHash();
        hCon.performHash();
    }

    @FXML
    public void onInsert() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mcs/modelsearcher/insert-view.fxml"));
            Parent root = loader.load();
            InsertViewController insertCon = loader.getController();

            Stage insertPopupStage = new Stage();
            insertCon.setStage(insertPopupStage);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/mcs/modelsearcher/style/main-view.css")).toExternalForm());

            insertPopupStage.setScene(scene);
            insertPopupStage.initModality(Modality.APPLICATION_MODAL); // disable main-view
            insertPopupStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening insert popup: " + e.getMessage());
        }
    }

    public void refreshFilePathLabel() {
        if (fileController != null && fileController.getFilePath() != null) {
            filePathLabel.setText(fileController.getFilePath());
        } else {
            filePathLabel.setText("No file selected");
        }
    }

    private void setTableColumn() {
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

        setPriceColumnFormatted(unitPrice);
        setPercentColumnEmptyIfZero(mgmtCost);
        setPriceColumnFormatted(estPrice);
        setPriceColumnFormatted(refPrice);

        insertNo.setPrefWidth(50);
        partCode.setPrefWidth(120);
        rev.setPrefWidth(35);
        apply1.setPrefWidth(91);
        apply2.setPrefWidth(91);
        blueprintDate.setPrefWidth(93);
        clientBlueprint.setPrefWidth(62);
        scan.setPrefWidth(33);
        selfBlueprint.setPrefWidth(61);
        category.setPrefWidth(140);
        name.setPrefWidth(300);
        spec.setPrefWidth(220);
        maker.setPrefWidth(81);
        vendor.setPrefWidth(81);
        unitPrice.setPrefWidth(80);
        mgmtCost.setPrefWidth(45);
        estPrice.setPrefWidth(80);
        refPrice.setPrefWidth(80);
        note.setPrefWidth(300);
    }

    private <T extends Number> void setPriceColumnFormatted(TableColumn<Excel, T> column) {
        NumberFormat nf = NumberFormat.getInstance();
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.longValue() == 0L) {
                    setText("");
                } else {
                    setText(nf.format(item));
                }
            }
        });
    }

    private <T extends Number> void setPercentColumnEmptyIfZero(TableColumn<Excel, T> column) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.intValue() == 0) {
                    setText("");
                } else {
                    setText(item + "%");
                }
            }
        });
    }

    @FXML
    private void foobar() {
        System.out.println("Column widths:");
        for (TableColumn<Excel, ?> col : excelData.getColumns()) {
            System.out.println(col.getText() + ": " + col.getWidth());
        }
        System.out.println("---");
    }
}
