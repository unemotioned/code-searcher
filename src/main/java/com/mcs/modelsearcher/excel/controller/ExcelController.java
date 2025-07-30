package com.mcs.modelsearcher.excel.controller;

import com.mcs.modelsearcher.excel.model.service.ExcelService;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.excel.model.vo.Hierarchy;
import com.mcs.modelsearcher.file.controller.FileController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelController {
    ExcelService eServ;
    FileController fCon;

    String excelPath;

    public ExcelController() {
        eServ = new ExcelService();

        fCon = new FileController();
        excelPath = fCon.selectPath();
    }

    public void clearDataTable() {
        int deleteRowResult = eServ.clearDataTable();
        System.out.println("ExcelController.deleteDataTable(): deleted row(s): " + deleteRowResult);
    }

    public void newDataTable() {
        try (FileInputStream fis = new FileInputStream(excelPath); Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            final int headerRowIndex = 4;

            ArrayList<Excel> excelList = new ArrayList<>();

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            while (rowIterator.hasNext()) {
                Excel excel = new Excel();
                Row row = rowIterator.next();

                if (row.getRowNum() < headerRowIndex) {
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

            final int headerRowIndex = 1;

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
            return String.valueOf((int) cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.toString());
        }
    }

    private int getPriceCellValue(Row row, int index, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(index);
        if (cell == null) return 0;

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
                if (evaluatedValue == null) return 0;

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
        if (cell == null) return 0;

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
        if (row == null) return null;

        Cell cell = row.getCell(revIndex);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private String getDateCellValue(Row row) {
        final int dateIndex = 6;
        Cell cell = row.getCell(dateIndex);

        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date dateValue = cell.getDateCellValue();
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(dateValue);
        }

        return null;
    }

    public List<Excel> uniSearch(ArrayList<String> keywordList) {
        return eServ.uniSearch(keywordList);
    }

    public void writeToExcel(Excel record) {
        eServ.writeToExcel(record);
    }

    public void insertToDb(Excel record) {
        int result = eServ.insertToDb(record);
        if (result == 1) {
            System.out.println("eCon.insertToDb: success");
        } else {
            System.out.println("eCon.insertToDb: fail");
        }
    }

    public void deleteFromExcel(String insertNo) {
        eServ.deleteFromExcel(insertNo);
    }

    public void deleteFromDb(String insertNo) {
        int result = eServ.deleteFromDb(insertNo);
        if (result >= 1) {
            System.out.println("ExcelController.deleteFromDb: success");
        } else {
            System.out.println("ExcelController.deleteFromDb: fail");
        }
    }
}
