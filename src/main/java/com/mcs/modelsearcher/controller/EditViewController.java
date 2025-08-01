package com.mcs.modelsearcher.controller;

import com.mcs.modelsearcher.controller.util.Util;
import com.mcs.modelsearcher.excel.controller.ExcelController;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditViewController {

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

  private Stage popupStage;

  Excel excel;
  ExcelController eCon;

  Util util;
  String originalInsertNo;

  public EditViewController() {
    eCon = new ExcelController();
    util = new Util();
  }

  public void setStage(Stage stage) {
    this.popupStage = stage;
    util = new Util();
  }

  @FXML
  public void initialize(Excel item) {
    originalInsertNo = item.getInsertNo();

    insertNo.setText(originalInsertNo);
    partCode.setText(item.getPartCode());
    rev.setText(item.getRev());
    apply1.setText(item.getApply1());
    apply2.setText(item.getApply2());
    blueprintDate.setText(Util.formatDateToShort(item.getBlueprintDate()));
    clientBlueprint.setText(item.getClientBlueprint());
    scan.setText(item.getScan());
    selfBlueprint.setText(item.getSelfBlueprint());
    category.setText(item.getCategory());
    name.setText(item.getName());
    spec.setText(item.getSpec());
    maker.setText(item.getMaker());
    vendor.setText(item.getVendor());
    unitPrice.setText(item.getUnitPrice() == 0 ? null : String.valueOf(item.getUnitPrice()));
    mgmtCost.setText(item.getMgmtCost() == 0 ? null : String.valueOf(item.getMgmtCost()));
    estPrice.setText(item.getEstPrice() == 0 ? null : String.valueOf(item.getEstPrice()));
    refPrice.setText(item.getRefPrice() == 0 ? null : String.valueOf(item.getRefPrice()));
    note.setText(item.getNote());
  }

  @FXML
  @SuppressWarnings("Duplicates")
  public void onEditClick() {
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
    excel.setOriginalInsertNo(originalInsertNo);

    eCon.editFromExcel(excel);
    eCon.updateFromDb(excel);

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
