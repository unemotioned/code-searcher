package com.mcs.modelsearcher.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private Stage popupStage;

    public void setStage(Stage stage) {
        this.popupStage = stage;
    }

    @FXML
    public void onSaveClick() {
        String insertNo = this.insertNo.getText();
        String partCode = this.partCode.getText();
        String rev = this.rev.getText();
        String apply1 = this.apply1.getText();
        String apply2 = this.apply2.getText();
        String blueprintDate = this.blueprintDate.getText();
        String clientBlueprint = this.clientBlueprint.getText();
        String scan = this.scan.getText();
        String selfBlueprint = this.selfBlueprint.getText();
        String category = this.category.getText();
        String name = this.name.getText();
        String spec = this.spec.getText();
        String maker = this.maker.getText();
        String vendor = this.vendor.getText();
        String unitPrice = this.unitPrice.getText();
        String mgmtCost = this.mgmtCost.getText();
        String estPrice = this.estPrice.getText();
        String refPrice = this.refPrice.getText();
        String note = this.note.getText();

        // TODO: Save to Excel and database

        // close
        popupStage.close();
    }
}
