package com.mcs.modelsearcher.controller;

import com.mcs.modelsearcher.excel.controller.ExcelController;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.file.controller.FileController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        addRecord();
        eCon.insertRecord(record);

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

    public void addRecord() {
        String[] data = {insertNo.getText(), partCode.getText(), rev.getText(), apply1.getText(), apply2.getText(), blueprintDate.getText(), clientBlueprint.getText(), scan.getText(), selfBlueprint.getText(), category.getText(), name.getText(), spec.getText(), maker.getText(), vendor.getText(), unitPrice.getText(), mgmtCost.getText(), estPrice.getText(), refPrice.getText(), note.getText()};

        FileController fCon = new FileController();
        String path = fCon.selectPath();

        final int firstIndex = 1;
        final int dateIndex = 5;
        final int percentIndex = 15;
        final int priceStart = 14;
        final int priceEnd = 17;

        final int insertNoindex = 0;

        // for left align
        final int partCodeIndex = 1;
        final int categoryIndex = 9;
        final int specIndex = 10;
        final int makerIndex = 11;
        final int noteIndex = 18;


        try (FileInputStream fis = new FileInputStream(path); Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            int newRowNum = sheet.getLastRowNum() + 1;
            Row newRow = sheet.createRow(newRowNum);

            int lastRowNum = sheet.getLastRowNum();
            while (lastRowNum >= 0 && isRowEmpty(sheet.getRow(lastRowNum))) {
                lastRowNum--;
            }

            // alignment, border, font
            CellStyle cellStyle = cellStyle(workbook);

            for (int i = 0; i < data.length; i++) {
                Cell cell = newRow.createCell(i + firstIndex);

                if (i == dateIndex) {
                    setDateCell(workbook, cell, cellStyle, data[i]);
                } else if (i == insertNoindex) {
                    setInsertNoCell(workbook, cell, cellStyle, data[i]);
                } else if (i == percentIndex) {
                    setPercentCell(workbook, cell, cellStyle, data[i]);
                } else if (i >= priceStart && i <= priceEnd) {
                    setPriceCell(workbook, cell, cellStyle, data[i]);
                } else if (i == partCodeIndex || i == categoryIndex || i == specIndex || i == makerIndex || i == noteIndex) {
                    alignLeft(workbook, cell, cellStyle);
                    cell.setCellValue(data[i]);
                } else {
                    cell.setCellValue(data[i]);
                    cell.setCellStyle(cellStyle);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(path)) {
                workbook.write(fos);
                System.out.println("Record added");
            }
        } catch (IOException e) {
            System.out.println("Error saving file" + e.getMessage());
        }
    }

    private void setPercentCell(Workbook workbook, Cell cell, CellStyle baseStyle, String input) {
        if (input == null || input.isEmpty()) {
            cell.setBlank();
            cell.setCellStyle(baseStyle);
            return;
        }

        CellStyle percentStyle = workbook.createCellStyle();
        percentStyle.cloneStyleFrom(baseStyle);

        DataFormat format = workbook.createDataFormat();
        percentStyle.setDataFormat(format.getFormat("0%")); // or "0.0%" for one decimal place

        try {
            double value = Double.parseDouble(input) / 100.0;
            cell.setCellValue(value);
        } catch (NumberFormatException e) {
            cell.setCellValue(input); // fallback as text
        }

        cell.setCellStyle(percentStyle);
    }

    private void setInsertNoCell(Workbook workbook, Cell cell, CellStyle baseStyle, String input) {
        if (input == null || input.isEmpty()) {
            cell.setBlank();
            cell.setCellStyle(baseStyle);
            return;
        }

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(baseStyle);

        if (input.matches("^\\d+$")) {
            // Only digits: treat as number
            try {
                double numericValue = Double.parseDouble(input);
                DataFormat format = workbook.createDataFormat();
                style.setDataFormat(format.getFormat("0")); // no decimal places

                cell.setCellValue(numericValue);
                cell.setCellStyle(style);
            } catch (NumberFormatException e) {
                // Fallback to text if something goes wrong
                cell.setCellValue(input);
                cell.setCellStyle(style);
            }
        } else {
            // Contains dash or other non-digit: treat as text
            cell.setCellValue(input);
            cell.setCellStyle(style);
        }
    }

    private void alignLeft(Workbook workbook, Cell cell, CellStyle baseStyle) {
        CellStyle alignLeft = workbook.createCellStyle();
        alignLeft.cloneStyleFrom(baseStyle);
        alignLeft.setAlignment(HorizontalAlignment.LEFT);

        cell.setCellStyle(alignLeft);
    }

    private void setPriceCell(Workbook workbook, Cell cell, CellStyle baseStyle, String input) {
        if (input == null || input.trim().isEmpty()) {
            cell.setBlank();
            cell.setCellStyle(baseStyle);
            return;
        }

        CellStyle numberStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();

        numberStyle.cloneStyleFrom(baseStyle);
        numberStyle.setDataFormat(format.getFormat("#,##0_);(#,##0)"));
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);

        try {
            double numericValue = Double.parseDouble(input.replace(",", ""));
            cell.setCellValue(numericValue);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing insert number: " + e.getMessage());
            cell.setBlank();
        }

        cell.setCellStyle(numberStyle);
    }

    private void setDateCell(Workbook workbook, Cell cell, CellStyle baseStyle, String input) {
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyMMdd");
        try {
            Date date = sdfInput.parse(input);

            cell.setCellValue(date);

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(baseStyle);
            dateStyle.setDataFormat((short) 14);

            cell.setCellStyle(dateStyle);
        } catch (ParseException e) {
            System.out.println("Invalid date format in blueprintDate: " + input);
            cell.setCellValue(input);
            cell.setCellStyle(baseStyle);
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    public CellStyle cellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setAlignment(HorizontalAlignment.CENTER); // CENTER, LEFT, RIGHT
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        HSSFWorkbook hssfWorkbook = (HSSFWorkbook) workbook;
        HSSFPalette palette = hssfWorkbook.getCustomPalette();
        // 42 since it's reserved for .xlx
        short customColorIndex = 42;
        // turn it into #969697
        palette.setColorAtIndex(customColorIndex, (byte) 150, (byte) 150, (byte) 151);
        style.setTopBorderColor(customColorIndex);
        style.setBottomBorderColor(customColorIndex);
        style.setLeftBorderColor(customColorIndex);
        style.setRightBorderColor(customColorIndex);

        Font font = workbook.createFont();
        font.setFontName("돋움");
        font.setFontHeightInPoints((short) 9);
        font.setBold(false);
        style.setFont(font);

        return style;
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
