package com.mcs.modelsearcher.hash.controller;

import com.mcs.modelsearcher.excel.controller.ExcelController;
import com.mcs.modelsearcher.file.controller.FileController;
import com.mcs.modelsearcher.hash.model.service.HashService;
import com.mcs.modelsearcher.hash.model.vo.SheetHash;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashController {
    FileController fCon;
    SheetHash sheetHash;
    HashService fServ;
    ExcelController excelCon;

    String excelPath;

    public HashController(FileController fileController) {
        fCon = fileController;
        sheetHash = new SheetHash();
        fServ = new HashService();
        excelCon = new ExcelController();

        excelPath = fCon.getFilePath();
    }

    public void performHash() {
        try (FileInputStream fis = new FileInputStream(excelPath); Workbook workbook = WorkbookFactory.create(fis)) {
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
            System.out.println("HashController.performHash(): Error while hashing Excel file." + e.getMessage());
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

        if (refHash != null) {
            if (refHash.equals(sheetHash.getHash())) {
                System.out.println("HashController.checkHash(): DB is up to date");
                System.out.println("HashController.checkHash(): Load DATA table from DB to UI");
            } else {
                updateHash(sheetHash);
                todo = "update";
                sheetSpecificDBAction(todo, sheetIndex);
            }
        } else {
            insertHash(sheetHash);
            sheetSpecificDBAction(todo, sheetIndex);
        }
    }

    public void sheetSpecificDBAction(String todo, int sheetIndex) {
        int bomSheet = 0;
        int hierarchySheet = 1;

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
        return fServ.selectHash(sheetName);
    }

    public void insertHash(SheetHash sheetHash) {
        int result = fServ.insertHash(sheetHash);

        if (result == 1) {
            System.out.println("HashController.insertHash(): success");
        } else {
            System.out.println("HashController.insertHash(): fail");
        }
    }

    public void updateHash(SheetHash sheetHash) {
        int hashUpdateResult = fServ.updateHash(sheetHash);

        if (hashUpdateResult == 1) {
            System.out.println("HashController.updateHash(): success");
        } else {
            System.out.println("HashController.updateHash(): fail");
        }
    }
}
