package com.mcs.modelsearcher.excel.controller;

import com.mcs.modelsearcher.excel.model.service.ExcelService;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.excel.model.vo.Hierarchy;
import com.mcs.modelsearcher.file.controller.FileController;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelController {
    ExcelService eServ;
    FileController fCon;

    String excelPath;

    public ExcelController(FileController fileController) {
        fCon = fileController;
        eServ = new ExcelService();

        excelPath = fCon.getFilePath();
    }

    public void clearDataTable() {
        int deleteRowResult = eServ.clearDataTable();
        System.out.println("ExcelController.deleteDataTable(): deleted row(s): " + deleteRowResult);
    }

    public void newDataTable() {
        try (FileInputStream fis = new FileInputStream(excelPath); Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            int headerRowIndex = 4;

            ArrayList<Excel> excelList = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Excel excel = new Excel();
                Row row = rowIterator.next();

                if (row.getRowNum() < headerRowIndex) {
                    continue;
                }

                excel.setInsertNo(getStringCellValue(row, 1));
                excel.setPartCode(getStringCellValue(row, 2));
                excel.setRev(getStringCellValue(row, 3));
                excel.setApply1(getStringCellValue(row, 4));
                excel.setApply2(getStringCellValue(row, 5));
                excel.setBlueprintDate(getStringCellValue(row, 6));
                excel.setClientBlueprint(getStringCellValue(row, 7));
                excel.setScan(getStringCellValue(row, 8));
                excel.setSelfBlueprint(getStringCellValue(row, 9));
                excel.setCategory(getStringCellValue(row, 10));
                excel.setName(getStringCellValue(row, 11));
                excel.setSpec(getStringCellValue(row, 12));
                excel.setMaker(getStringCellValue(row, 13));
                excel.setVendor(getStringCellValue(row, 14));
                excel.setUnitPrice((int) getNumericCellValue(row, 15));
                excel.setMgmtCost((int) getNumericCellValue(row, 16));
                excel.setEstPrice((int) getNumericCellValue(row, 17));
                excel.setRefPrice((int) getNumericCellValue(row, 18));
                excel.setNote(getStringCellValue(row, 19));

                excelList.add(excel);
            }

            // do insert
            int newDataTableResult = eServ.newDataTable(excelList);
            if (newDataTableResult == 1) {
                System.out.println("ExcelController: newDataTable() success");
            } else {
                System.out.println("ExcelController: newDataTable() fail");
            }

        } catch (IOException e) {
            System.out.println("Error while opening Excel file: " + e.getMessage());
        }
    }

    public void clearHierarchyTable() {
        int deleteRowResult = eServ.clearHierarchyTable();
        System.out.println("ExcelController.deleteHierarchyTable(): deleted row(s): " + deleteRowResult);
    }

    public void newHierarchyTable() {
        try (FileInputStream fis = new FileInputStream(excelPath); Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            // DEBUG
            System.out.println("sheetName: " + sheet.getSheetName());

            Iterator<Row> rowIterator = sheet.iterator();

            int headerRowIndex = 1;

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
                System.out.println("ExcelController: newDataTable() success");
            } else {
                System.out.println("ExcelController: newDataTable() fail");
            }

        } catch (IOException e) {
            System.out.println("Error while opening Excel file: " + e.getMessage());
        }
    }

    private String getStringCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";

        if (cell.getCellType() == CellType.NUMERIC) {
            // truncate to integer
            return String.valueOf((int) cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.toString());
        }
    }

    private double getNumericCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return 0;
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }
}
