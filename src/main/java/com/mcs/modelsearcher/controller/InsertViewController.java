package com.mcs.modelsearcher.controller;

import com.mcs.modelsearcher.controller.util.Util;
import com.mcs.modelsearcher.excel.controller.ExcelController;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.file.controller.FileController;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class InsertViewController {

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

  Excel excel;
  ExcelController eCon;
  FileController fCon;
  String filePath;
  Util util;

  private Stage popupStage;

  public InsertViewController() {
    eCon = new ExcelController();
    fCon = new FileController();
    filePath = fCon.selectPath();
    util = new Util();
  }

  public void setStage(Stage stage) {
    this.popupStage = stage;
  }

  @FXML
  public void initialize() {
    insertNo.setText(getLastInsertNo());
    rev.setText("000");
  }

  public String getLastInsertNo() {
    String lastInsertNo = "";
    final byte bomSheet = 0;
    final byte insertNoCellIndex = 1;

    try (FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fis)) {
      Sheet sheet = workbook.getSheetAt(bomSheet);

      for (Row row : sheet) {
        Cell cell = row.getCell(insertNoCellIndex);

        if (cell != null && cell.getCellType() != CellType.BLANK) {
          lastInsertNo = cell.toString();
        }
      }

    } catch (IOException e) {
      System.out.println("insertCon.getLastInsertNo - while opening: " + e.getMessage());
    }
    return String.valueOf(fabricateInsertNo(lastInsertNo));
  }

  private int fabricateInsertNo(String lastInsertNo) {
    if (lastInsertNo == null || lastInsertNo.isEmpty()) {
      return -1;
    }

    try {
      int dashIndex = lastInsertNo.indexOf('-');
      String numberPart =
          (dashIndex != -1) ? lastInsertNo.substring(0, dashIndex).trim() : lastInsertNo.trim();

      double parsedDouble = Double.parseDouble(numberPart);
      return (int) parsedDouble + 1;

    } catch (NumberFormatException e) {
      System.out.println("insertCon.fabricateInsertNo: " + e.getMessage());
      return -1;
    }
  }

  @FXML
  @SuppressWarnings("Duplicates")
  public void onSaveClick() {
    excel = new Excel();
    excel.setInsertNo(insertNo.getText());
    excel.setPartCode(partCode.getText());
    excel.setRev(rev.getText());
    excel.setApply1(apply1.getText());
    excel.setApply2(apply2.getText());
    excel.setBlueprintDate(blueprintDate.getText());
    excel.setClientBlueprint(util.bluePrintAndScan(clientBlueprint));
    excel.setScan(util.bluePrintAndScan(scan));
    excel.setSelfBlueprint(util.bluePrintAndScan(selfBlueprint));
    excel.setCategory(category.getText());
    excel.setName(name.getText());
    excel.setSpec(spec.getText());
    excel.setMaker(maker.getText());
    excel.setVendor(vendor.getText());
    excel.setUnitPrice(util.priceInputToInt(unitPrice));
    excel.setMgmtCost(util.priceInputToInt(mgmtCost));
    excel.setEstPrice(util.priceInputToInt(estPrice));
    excel.setRefPrice(util.priceInputToInt(refPrice));
    excel.setNote(note.getText());

    int writeResult = eCon.writeToExcel(excel);
    if (writeResult == 1) {
      eCon.insertToDb(excel);
    }

    if (popupStage != null) {
      popupStage.close();
    }
  }

  @FXML
  public void onCancel() {
    if (popupStage != null) {
      popupStage.close();
    }
  }
}
