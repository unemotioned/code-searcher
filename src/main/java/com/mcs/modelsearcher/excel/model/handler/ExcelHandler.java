package com.mcs.modelsearcher.excel.model.handler;

import com.mcs.modelsearcher.controller.MainController;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.file.controller.FileController;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelHandler {

  MainController mCon;

  public ExcelHandler() {
    mCon = new MainController();
  }

  @SuppressWarnings("Duplicates")
  public void writeToExcel(Excel record) {
    String[] data = {record.getInsertNo(), record.getPartCode(), record.getRev(),
        record.getApply1(), record.getApply2(), record.getBlueprintDate(),
        record.getClientBlueprint(), record.getScan(), record.getSelfBlueprint(),
        record.getCategory(), record.getName(), record.getSpec(), record.getMaker(),
        record.getVendor(),
        record.getUnitPrice() == 0 ? null : String.valueOf(record.getUnitPrice()),
        record.getMgmtCost() == 0 ? null : String.valueOf(record.getMgmtCost()),
        record.getEstPrice() == 0 ? null : String.valueOf(record.getEstPrice()),
        record.getRefPrice() == 0 ? null : String.valueOf(record.getRefPrice()), record.getNote()};

    FileController fCon = new FileController();
    String path = fCon.selectPath();

    final byte firstIndex = 1;
    final byte dateIndex = 5;
    final byte percentIndex = 15;
    final byte priceStart = 14;
    final byte priceEnd = 17;

    final byte insertNoindex = 0;

    // for left align
    final byte partCodeIndex = 1;
    final byte categoryIndex = 9;
    final byte specIndex = 10;
    final byte makerIndex = 11;
    final byte noteIndex = 18;

    try (FileInputStream fis = new FileInputStream(
        path); BufferedInputStream bis = new BufferedInputStream(
        fis); Workbook workbook = WorkbookFactory.create(bis)) {
      Sheet sheet = workbook.getSheetAt(0);

      int newRowNum = findFirstEmptyRow(sheet);
      Row newRow = sheet.createRow(newRowNum);

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
        } else if (i == partCodeIndex || i == categoryIndex || i == specIndex || i == makerIndex
            || i == noteIndex) {
          alignLeft(workbook, cell, cellStyle);
          cell.setCellValue(data[i]);
        } else {
          cell.setCellValue(data[i]);
          cell.setCellStyle(cellStyle);
        }
      }

      try (FileOutputStream fos = new FileOutputStream(
          path); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
        workbook.write(bos);
        System.out.println("eHand.writeToExcel: success");
      } catch (IOException e) {
        System.out.println("eHand.writeToExcel - while saving: " + e.getMessage());
        mCon.errorModel(e.getMessage());
      }
    } catch (IOException e) {
      System.out.println("eHand.writeToExcel - while opening: " + e.getMessage());
    }
  }

  private int findFirstEmptyRow(Sheet sheet) {
    final byte startOfDataIndex = 5;
    int lastRow = sheet.getLastRowNum();
    for (int i = startOfDataIndex; i <= lastRow; i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row)) {
        return i;
      }
    }
    return lastRow + 1;
  }

  private boolean isRowEmpty(Row row) {
    if (row == null) {
      return true;
    }
    for (Cell cell : row) {
      if (cell != null && cell.getCellType() != CellType.BLANK) {
        return false;
      }
    }
    return true;
  }

  private CellStyle cellStyle(Workbook workbook) {
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
    CellStyle dateStyle = workbook.createCellStyle();
    dateStyle.cloneStyleFrom(baseStyle);
    dateStyle.setDataFormat((short) 14);

    if (input == null || input.trim().isEmpty()) {
      cell.setBlank();
      cell.setCellStyle(dateStyle);
      return;
    }

    SimpleDateFormat sdfInput = new SimpleDateFormat("yyMMdd");
    try {
      Date date = sdfInput.parse(input);
      cell.setCellValue(date);
    } catch (ParseException e) {
      System.out.println("eHand.setDateCel - invalid date format: " + input);
      cell.setCellValue(input);
    }
    cell.setCellStyle(dateStyle);
  }

  public void deleteFromExcel(String insertNo) {
    FileController fCon = new FileController();
    String path = fCon.selectPath();

    try (FileInputStream fis = new FileInputStream(
        path); BufferedInputStream bis = new BufferedInputStream(
        fis); Workbook workbook = new HSSFWorkbook(bis)) {
      final byte bomSheetIndex = 0;
      final byte insertNoColIndex = 1;
      boolean rowDeleted = false;

      Sheet sheet = workbook.getSheetAt(bomSheetIndex);

      for (int i = 0; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        Cell cell = row.getCell(insertNoColIndex);
        if (cell != null) {
          String value = getString(cell);

          if (insertNo.equals(value)) {
            removeRow(sheet, i);
            rowDeleted = true;
            break;
          }
        }
      }

      if (rowDeleted) {
        try (FileOutputStream fos = new FileOutputStream(
            path); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
          workbook.write(bos);
          System.out.println("eHand.deleteFromExcel: success");
        } catch (IOException e) {
          System.out.println("eHand.deleteFromExcel - while saving " + e.getMessage());
          mCon.errorModel(e.getMessage());
        }
      }
    } catch (IOException e) {
      System.out.println("eCon.deleteFromExcel - while opening: " + e.getMessage());
    }
  }

  private static String getString(Cell cell) {
    String value = null;

    if (cell.getCellType() == CellType.STRING) {
      value = cell.getStringCellValue();
    } else if (cell.getCellType() == CellType.NUMERIC) {
      double num = cell.getNumericCellValue();
      long longVal = (long) num;
      // Check if it's a whole number
      if (num == longVal) {
        value = String.valueOf(longVal);
      } else {
        value = String.valueOf(num); // unlikely, but safe fallback
      }
    }
    return value;
  }

  private static void removeRow(Sheet sheet, int rowIndex) {
    int lastRowNum = sheet.getLastRowNum();
    if (rowIndex >= 0 && rowIndex < lastRowNum) {
      sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
    } else if (rowIndex == lastRowNum) {
      Row row = sheet.getRow(rowIndex);
      if (row != null) {
        sheet.removeRow(row);
      }
    }
  }

  @SuppressWarnings("Duplicates")
  public void editFromExcel(Excel record) {
    String[] data = {record.getInsertNo(), record.getPartCode(), record.getRev(),
        record.getApply1(), record.getApply2(), record.getBlueprintDate(),
        record.getClientBlueprint(), record.getScan(), record.getSelfBlueprint(),
        record.getCategory(), record.getName(), record.getSpec(), record.getMaker(),
        record.getVendor(),
        record.getUnitPrice() == 0 ? null : String.valueOf(record.getUnitPrice()),
        record.getMgmtCost() == 0 ? null : String.valueOf(record.getMgmtCost()),
        record.getEstPrice() == 0 ? null : String.valueOf(record.getEstPrice()),
        record.getRefPrice() == 0 ? null : String.valueOf(record.getRefPrice()), record.getNote()};

    FileController fCon = new FileController();
    String path = fCon.selectPath();

    final int firstIndex = 1;
    final int dateIndex = 5;
    final int percentIndex = 15;
    final int priceStart = 14;
    final int priceEnd = 17;
    final int insertNoColIndex = 1;

    final int partCodeIndex = 1;
    final int categoryIndex = 9;
    final int specIndex = 10;
    final int makerIndex = 11;
    final int noteIndex = 18;

    try (FileInputStream fis = new FileInputStream(
        path); BufferedInputStream bis = new BufferedInputStream(
        fis); Workbook workbook = WorkbookFactory.create(bis)) {
      final byte bomSheet = 0;
      final byte startOfData = 5;
      Sheet sheet = workbook.getSheetAt(bomSheet);
      boolean updated = false;

      for (int i = startOfData; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        Cell cell = row.getCell(insertNoColIndex);
        if (cell == null) {
          continue;
        }

        String value = getString(cell);
        if (!record.getOriginalInsertNo().equals(value)) {
          continue;
        }

        CellStyle baseStyle = cellStyle(workbook);

        for (int j = 0; j < data.length; j++) {
          int cellIndex = j + firstIndex;
          Cell targetCell = row.getCell(cellIndex);
          if (targetCell == null) {
            targetCell = row.createCell(cellIndex);
          }

          if (j == dateIndex) {
            setDateCell(workbook, targetCell, baseStyle, data[j]);
          } else if (j == 0) {
            setInsertNoCell(workbook, targetCell, baseStyle, data[j]);
          } else if (j == percentIndex) {
            setPercentCell(workbook, targetCell, baseStyle, data[j]);
          } else if (j >= priceStart && j <= priceEnd) {
            setPriceCell(workbook, targetCell, baseStyle, data[j]);
          } else if (j == partCodeIndex || j == categoryIndex || j == specIndex || j == makerIndex
              || j == noteIndex) {
            alignLeft(workbook, targetCell, baseStyle);
            targetCell.setCellValue(data[j]);
          } else {
            targetCell.setCellValue(data[j]);
            targetCell.setCellStyle(baseStyle);
          }
        }

        updated = true;
        break;
      }

      if (updated) {
        try (FileOutputStream fos = new FileOutputStream(
            path); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
          workbook.write(bos);
          System.out.println("eHand.editFromExcel - success " + record.getInsertNo());
        } catch (Exception e) {
          System.out.println("eHand.editFromExcel - while saving: " + record.getInsertNo());
          mCon.errorModel(e.getMessage());
        }
      } else {
        System.out.println("eHand.editFromExcel: update canceled");
      }
    } catch (IOException e) {
      System.out.println("eHand.editFromExcel- while opening: " + e.getMessage());
    }
  }
}
