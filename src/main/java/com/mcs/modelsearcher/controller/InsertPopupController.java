package com.mcs.modelsearcher.controller;

import com.mcs.modelsearcher.excel.controller.ExcelController;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.file.controller.FileController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;

public class InsertPopupController {
    // @formatter:off
    @FXML private TextField insertNo;
    @FXML private TextField partCode;
    @FXML private TextField rev;
    @FXML private TextField apply1;
    @FXML private TextField apply2;
    @FXML private TextField blueprintDate;
    @FXML private TextField clientBlueprint;
    @FXML private TextField scan;
    @FXML private TextField selfBlueprint;
    @FXML private TextField category;
    @FXML private TextField name;
    @FXML private TextField spec;
    @FXML private TextField maker;
    @FXML private TextField vendor;
    @FXML private TextField unitPrice;
    @FXML private TextField mgmtCost;
    @FXML private TextField estPrice;
    @FXML private TextField refPrice;
    @FXML private TextField note;
    // @formatter:on

    Excel record;
    ExcelController eCon;
    FileController fCon;
    String filePath;

    private Stage popupStage;

    public InsertPopupController() {
        eCon = new ExcelController();
        fCon = new FileController();
        filePath = fCon.selectPath();
    }

    @FXML
    public void initialize() {
        insertNo.setText(getLastInsertNo());
        rev.setText("000");

        // for testing
        partCode.setText("D400-59798A");
        apply1.setText("CLT Handler");
        blueprintDate.setText("250729");
        clientBlueprint.setText("○");
        category.setText("Camera");
        name.setText("LED-CLT HANDLER BAR LIGHT L");
        spec.setText("MLC-C24B-350W");
        maker.setText("BASLER");
        vendor.setText("바슬러코리아");
        unitPrice.setText("75000");
        mgmtCost.setText("10");
        estPrice.setText("7500000");
        note.setText("(주의) foobar");
    }

    public String getLastInsertNo() {
        String lastInsertNo = "";
        int insertNoCellIndex = 1;

        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(insertNoCellIndex);

                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    lastInsertNo = cell.toString();
                }
            }

        } catch (IOException e) {
            System.out.println("InsertPopCon.getLastInsertNo: " + e.getMessage());
        }

        return String.valueOf(fabricateInsertNo(lastInsertNo));
    }

    private int fabricateInsertNo(String lastInsertNo) {
        if (lastInsertNo == null || lastInsertNo.isEmpty()) {
            return -1;
        }

        try {
            int dashIndex = lastInsertNo.indexOf('-');
            String numberPart = (dashIndex != -1) ? lastInsertNo.substring(0, dashIndex).trim() : lastInsertNo.trim();

            double parsedDouble = Double.parseDouble(numberPart);
            return (int) parsedDouble + 1;

        } catch (NumberFormatException e) {
            System.out.println("Error parsing insert number: " + e.getMessage());
            return -1;
        }
    }

    @FXML
    public void onSaveClick() {
        record = new Excel();

        record.setInsertNo(insertNo.getText());
        record.setPartCode(partCode.getText());
        record.setRev(rev.getText());
        record.setApply1(apply1.getText());
        record.setApply2(apply2.getText());
        record.setBlueprintDate(blueprintDate.getText());
        record.setClientBlueprint(clientBlueprint.getText());
        record.setScan(scan.getText());
        record.setSelfBlueprint(selfBlueprint.getText());
        record.setCategory(category.getText());
        record.setName(name.getText());
        record.setSpec(spec.getText());
        record.setMaker(maker.getText());
        record.setVendor(vendor.getText());
        record.setUnitPrice(priceInputToInt(unitPrice));
        record.setMgmtCost(priceInputToInt(mgmtCost));
        record.setEstPrice(priceInputToInt(estPrice));
        record.setRefPrice(priceInputToInt(refPrice));
        record.setNote(note.getText());

        eCon.writeToExcel(record);
        eCon.insertToDb(record);

        if (popupStage != null) {
            popupStage.close();
        }
    }

    public int priceInputToInt(TextField tf) {
        try {
            if (tf != null) {
                return Integer.parseInt(tf.getText().trim());
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setStage(Stage stage) {
        this.popupStage = stage;
    }

    public void onCancel() {
        if (popupStage != null) {
            popupStage.close();
        }
    }
}
