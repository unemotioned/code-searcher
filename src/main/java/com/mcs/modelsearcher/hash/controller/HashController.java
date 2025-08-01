package com.mcs.modelsearcher.hash.controller;

import com.mcs.modelsearcher.excel.controller.ExcelController;
import com.mcs.modelsearcher.file.controller.FileController;
import com.mcs.modelsearcher.hash.model.service.HashService;
import com.mcs.modelsearcher.hash.model.vo.SheetHash;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class HashController {

  FileController fCon;
  String path;

  SheetHash sheetHash;
  HashService hServ;
  ExcelController excelCon;

  public HashController() {
    fCon = new FileController();
    path = fCon.selectPath();

    sheetHash = new SheetHash();
    hServ = new HashService();
    excelCon = new ExcelController();
  }

  public void performHash() {
    try (FileInputStream fis = new FileInputStream(path);
        BufferedInputStream bis = new BufferedInputStream(fis);
        Workbook workbook = WorkbookFactory.create(bis)) {
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        Sheet sheet = workbook.getSheetAt(i);
        StringBuilder combinedData = combineCells(sheet);

        String hashed = hashData(combinedData.toString());
        String sheetName = sheet.getSheetName();
        sheetHash.setSheet(sheetName);
        sheetHash.setHash(hashed);

        checkHash(sheetHash, i);
      }
    } catch (IOException e) {
      System.out.println("hCon.performHash: " + e.getMessage());
    }
  }

  private static StringBuilder combineCells(Sheet sheet) {
    StringBuilder combinedData = new StringBuilder();

    for (Row row : sheet) {
      for (Cell cell : row) {
        switch (cell.getCellType()) {
          case STRING -> combinedData.append(cell.getStringCellValue());
          case NUMERIC -> combinedData.append(cell.getNumericCellValue());
          case BOOLEAN -> combinedData.append(cell.getBooleanCellValue());
          case FORMULA -> combinedData.append(cell.getCellFormula());
          case BLANK -> combinedData.append(" ");
        }
      }
    }
    return combinedData;
  }

  public String hashData(String data) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(data.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : hashBytes) {
        // %02x -> binary into 2 digit hex code
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Hash failed", e);
    }
  }

  public void checkHash(SheetHash sheetHash, int sheetIndex) {
    String refHash = selectHash(sheetHash.getSheet());
    String todo = "";

    if (refHash == null) {
      insertHash(sheetHash);
      sheetSpecificDBAction(todo, sheetIndex);
    } else if (!refHash.equals(sheetHash.getHash())) {
      updateHash(sheetHash);
      todo = "update";
      sheetSpecificDBAction(todo, sheetIndex);
    }
  }

  public void sheetSpecificDBAction(String todo, int sheetIndex) {
    final byte bomSheet = 0;
    final byte hierarchySheet = 1;

    if (sheetIndex == bomSheet) {
      // To update, delete every records inside the table and insert fresh.
      if (todo.equals("update")) {
        excelCon.clearDataTable();
      }
      excelCon.newDataTable();

    } else if (sheetIndex == hierarchySheet) {
      if (todo.equals("update")) {
        excelCon.clearHierarchyTable();
      }
      excelCon.newHierarchyTable();
    }
  }

  public String selectHash(String sheetName) {
    return hServ.selectHash(sheetName);
  }

  public void insertHash(SheetHash sheetHash) {
    int result = hServ.insertHash(sheetHash);

    if (result == 1) {
      System.out.println("hCon.insertHash: success");
    } else {
      System.out.println("hCon.insertHash: fail");
    }
  }

  public void updateHash(SheetHash sheetHash) {
    int hashUpdateResult = hServ.updateHash(sheetHash);

    if (hashUpdateResult == 1) {
      System.out.println("hCon.updateHash: success");
    } else {
      System.out.println("hCon.updateHash: fail");
    }
  }

  public void fakeHash() {
    int result = hServ.fakeHash();
    if (result == 1) {
      System.out.println("hCon.fakeHash: success");
    } else {
      System.out.println("hCon.fakeHash: fail");
    }
  }
}
