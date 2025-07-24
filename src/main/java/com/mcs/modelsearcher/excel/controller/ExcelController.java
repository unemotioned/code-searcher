package com.mcs.modelsearcher.excel.controller;

import com.mcs.modelsearcher.excel.model.service.ExcelService;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.excel.model.vo.Hierarchy;
import com.mcs.modelsearcher.file.controller.FileController;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
            // DEBUG
            System.out.println("sheetName: " + sheet.getSheetName());

            Iterator<Row> rowIterator = sheet.iterator();

            int headerRowIndex = 4;

            ArrayList<Excel> excelList = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Excel excel = new Excel();
                Row row = rowIterator.next();

                if (row.getRowNum() < headerRowIndex) {
                    continue;
                }

                excel.setInsertNo((int) getNumericCellValue(row, 1));
                excel.setModelNo(getStringCellValue(row, 2));
                excel.setRev(getStringCellValue(row, 3));
                excel.setApply1(getStringCellValue(row, 4));
                excel.setApply2(getStringCellValue(row, 5));
                excel.setApply3(getStringCellValue(row, 6));
                excel.setBluePrint(getBooleanCellValue(row));
                excel.setBluePrintDate(getDateCellValue(row));
                excel.setCategory(getStringCellValue(row, 9));
                excel.setName(getStringCellValue(row, 10));
                excel.setSpec(getStringCellValue(row, 11));
                excel.setMaker(getStringCellValue(row, 12));
                excel.setVendor(getStringCellValue(row, 13));
                excel.setUnitPrice((int) getNumericCellValue(row, 14));
                excel.setMgmtCost((int) getNumericCellValue(row, 15));
                excel.setEstPrice((int) getNumericCellValue(row, 16));
                excel.setNote(getStringCellValue(row, 17));

                excelList.add(excel);
            }

            // DEBUG
            for (Excel excel : excelList) {
                System.out.println(excel.toString());
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

                hierarchy.setParent_no((int) getNumericCellValue(row, 0));
                hierarchy.setChild_no((int) getNumericCellValue(row, 1));

                hList.add(hierarchy);
            }

            // DEBUG
            for (Hierarchy hierarchy : hList) {
                System.out.println(hierarchy.toString());
            }

            // do insert
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
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : cell.toString();
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

    private boolean getBooleanCellValue(Row row) {
        Cell cell = row.getCell(7);
        if (cell == null) return false;

        if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            return Boolean.parseBoolean(cell.getStringCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue() != 0;
        }
        return false;
    }

    private Date getDateCellValue(Row row) {
        Cell cell = row.getCell(8);
        if (cell == null) return null;
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        }
        return null;
    }
}
