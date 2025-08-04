package com.mcs.codesearcher.excel.controller;

import com.mcs.codesearcher.controller.util.Util;
import com.mcs.codesearcher.excel.model.handler.ExcelHandler;
import com.mcs.codesearcher.excel.model.service.ExcelService;
import com.mcs.codesearcher.excel.model.vo.Excel;
import com.mcs.codesearcher.excel.model.vo.Hierarchy;
import com.mcs.codesearcher.file.controller.FileController;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelController {

  FileController fCon;
  String excelPath;
  ExcelService eServ;
  ExcelHandler eHan;

  public ExcelController() {
    fCon = new FileController();
    excelPath = fCon.selectPath();
    eServ = new ExcelService();
    eHan = new ExcelHandler();
  }

  public void clearDataTable() {
    int deleteRowResult = eServ.clearDataTable();
    System.out.println("eCon.deleteDataTable: deleted row(s): " + deleteRowResult);
  }

  public void newDataTable() {
    try (FileInputStream fis = new FileInputStream(excelPath);
        BufferedInputStream bis = new BufferedInputStream(fis);
        Workbook workbook = WorkbookFactory.create(bis)) {
      final byte bomSheet = 0;
      final byte headerRowIndex = 4;
      Sheet sheet = workbook.getSheetAt(bomSheet);
      Iterator<Row> rowIterator = sheet.iterator();
      ArrayList<Excel> excelList = new ArrayList<>();
      FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

      while (rowIterator.hasNext()) {
        Excel excel = new Excel();
        Row row = rowIterator.next();

        if (row.getRowNum() < headerRowIndex) {
          continue;
        }

        String insertNo = getStringCellValue(row, 1);
        if (insertNo == null || insertNo.trim().isEmpty()) {
          continue;
        }

        excel.setInsertNo(getStringCellValue(row, 1));
        excel.setPartCode(getStringCellValue(row, 2));
        excel.setRev(getRevValue(row));
        excel.setApply1(getStringCellValue(row, 4));
        excel.setApply2(getStringCellValue(row, 5));
        excel.setBlueprintDate(getDateCellValue(row));
        excel.setClientBlueprint(getStringCellValue(row, 7));
        excel.setScan(getStringCellValue(row, 8));
        excel.setSelfBlueprint(getStringCellValue(row, 9));
        excel.setCategory(getStringCellValue(row, 10));
        excel.setName(getStringCellValue(row, 11));
        excel.setSpec(getStringCellValue(row, 12));
        excel.setMaker(getStringCellValue(row, 13));
        excel.setVendor(getStringCellValue(row, 14));
        excel.setUnitPrice(getPriceCellValue(row, 15, evaluator));
        excel.setMgmtCost(getPercentCellValue(row, evaluator));
        excel.setEstPrice(getPriceCellValue(row, 17, evaluator));
        excel.setRefPrice(getPriceCellValue(row, 18, evaluator));
        excel.setNote(getStringCellValue(row, 19));

        excelList.add(excel);
      }

      int newDataTableResult = eServ.newDataTable(excelList);
      if (newDataTableResult == 1) {
        System.out.println("eCon.newDataTable: success");
      } else {
        System.out.println("eCon.newDataTable: fail");
      }
    } catch (IOException e) {
      System.out.println("eCon.newDataTable - error while opening file: " + e.getMessage());
    }
  }

  public void clearHierarchyTable() {
    int deleteRowResult = eServ.clearHierarchyTable();
    System.out.println("eCon.deleteHierarchyTable - deleted row(s): " + deleteRowResult);
  }

  public void newHierarchyTable() {
    try (FileInputStream fis = new FileInputStream(excelPath);
        BufferedInputStream bis = new BufferedInputStream(fis);
        Workbook workbook = WorkbookFactory.create(bis)) {
      final byte hierarchySheet = 1;
      final byte headerRowIndex = 1;
      Sheet sheet = workbook.getSheetAt(hierarchySheet);
      Iterator<Row> rowIterator = sheet.iterator();
      ArrayList<Hierarchy> hList = new ArrayList<>();

      while (rowIterator.hasNext()) {
        Hierarchy hierarchy = new Hierarchy();
        Row row = rowIterator.next();

        if (row.getRowNum() < headerRowIndex) {
          continue;
        }

        hierarchy.setParent_no(getStringCellValue(row, 0));
        hierarchy.setChild_no(getStringCellValue(row, 1));

        hList.add(hierarchy);
      }

      int newHierarchyTableResult = eServ.newHierarchyTable(hList);
      if (newHierarchyTableResult == 1) {
        System.out.println("eCon.newHierarchyTable: success");
      } else {
        System.out.println("eCon.newHierarchyTable: fail");
      }
    } catch (IOException e) {
      System.out.println("eCon.newHierarchyTable - while opening: " + e.getMessage());
    }
  }

  private String getStringCellValue(Row row, int index) {
    Cell cell = row.getCell(index);
    if (cell == null) {
      return "";
    }

    if (cell.getCellType() == CellType.NUMERIC) {
      return String.valueOf((int) cell.getNumericCellValue());
    } else {
      return String.valueOf(cell.toString());
    }
  }

  private int getPriceCellValue(Row row, int index, FormulaEvaluator evaluator) {
    Cell cell = row.getCell(index);
    if (cell == null) {
      return 0;
    }

    switch (cell.getCellType()) {
      case NUMERIC:
        return (int) cell.getNumericCellValue();

      case STRING:
        try {
          return (int) Double.parseDouble(cell.getStringCellValue());
        } catch (NumberFormatException e) {
          return 0;
        }

      case FORMULA:
        CellValue evaluatedValue = evaluator.evaluate(cell);
        if (evaluatedValue == null) {
          return 0;
        }

        switch (evaluatedValue.getCellType()) {
          case NUMERIC:
            return (int) evaluatedValue.getNumberValue();
          case STRING:
            try {
              return (int) Double.parseDouble(evaluatedValue.getStringValue());
            } catch (NumberFormatException e) {
              return 0;
            }
          default:
            return 0;
        }

      default:
        return 0;
    }
  }

  private int getPercentCellValue(Row row, FormulaEvaluator evaluator) {
    final int percentCellIndex = 16;
    Cell cell = row.getCell(percentCellIndex);
    if (cell == null) {
      return 0;
    }

    switch (cell.getCellType()) {
      case NUMERIC:
        return (int) Math.round(cell.getNumericCellValue() * 100);
      case FORMULA:
        CellValue evaluated = evaluator.evaluate(cell);
        if (evaluated != null && evaluated.getCellType() == CellType.NUMERIC) {
          return (int) Math.round(evaluated.getNumberValue() * 100);
        }
        return 0;
      case STRING:
        try {
          double val = Double.parseDouble(cell.getStringCellValue().replace("%", "").trim());
          return (int) Math.round(val);
        } catch (NumberFormatException e) {
          return 0;
        }
      default:
        return 0;
    }
  }

  public String getRevValue(Row row) {
    final int revIndex = 3;
    if (row == null) {
      return null;
    }

    Cell cell = row.getCell(revIndex);
    if (cell == null) {
      return null;
    }

    if (cell.getCellType() == CellType.STRING) {
      return cell.getStringCellValue();
    } else if (cell.getCellType() == CellType.NUMERIC) {
      return String.valueOf(cell.getNumericCellValue());
    }
    return null;
  }

  private String getDateCellValue(Row row) {
    final byte dateIndex = 6;
    Cell cell = row.getCell(dateIndex);

    if (cell == null || cell.getCellType() == CellType.BLANK) {
      return null;
    }

    if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
      Date dateValue = cell.getDateCellValue();
      return new java.text.SimpleDateFormat("yy-MM-dd").format(dateValue);
    }
    return null;
  }

  public List<Excel> uniSearch(ArrayList<String> keywordList) {
    return eServ.uniSearch(keywordList);
  }

  public int writeToExcel(Excel record) {
    return eHan.writeToExcel(record);
  }

  public void insertToDb(Excel excel) {
    String shortDate = excel.getBlueprintDate();
    String longDate = Util.formatDateToLong(shortDate);
    excel.setBlueprintDate(longDate);

    int result = eServ.insertToDb(excel);
    if (result == 1) {
      System.out.println("eCon.insertToDb: success");
    } else {
      System.out.println("eCon.insertToDb: fail");
    }
  }

  public int deleteFromExcel(String insertNo) {
    return eHan.deleteFromExcel(insertNo);
  }

  public void deleteFromDb(String insertNo) {
    int result = eServ.deleteFromDb(insertNo);
    if (result == 1) {
      System.out.println("eCon.deleteFromDb: success");
    } else {
      System.out.println("eCon.deleteFromDb: fail");
    }
  }

  public int editFromExcel(Excel excel) {
    return eHan.editFromExcel(excel);
  }

  public void updateFromDb(Excel excel) {
    String shortDate = excel.getBlueprintDate();
    String longDate = Util.formatDateToLong(shortDate);
    excel.setBlueprintDate(longDate);

    int result = eServ.updateFromDb(excel);
    if (result == 1) {
      System.out.println("eCon.updateFromDb: success");
    } else {
      System.out.println("eCon.updateFromDb: fail");
    }
  }
}
