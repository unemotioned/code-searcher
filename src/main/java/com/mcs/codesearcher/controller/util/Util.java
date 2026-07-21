package com.mcs.codesearcher.controller.util;

import com.mcs.codesearcher.excel.model.vo.Excel;
import javafx.scene.control.TextField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public String[] insertValueToExcel(Excel excel) {
        return new String[]{
                excel.getInsertNo(),
                excel.getPartCode(),
                excel.getRev(),
                excel.getApply1(),
                excel.getApply2(),
                excel.getBlueprintDate(),
                excel.getClientBlueprint(),
                excel.getScan(),
                excel.getSelfBlueprint(),
                excel.getCategory(),
                excel.getName(),
                excel.getSpec(),
                excel.getMaker(),
                excel.getVendor(),
                excel.getUnitPrice() == 0 ? null : String.valueOf(excel.getUnitPrice()),
                excel.getMgmtCost() == 0 ? null : String.valueOf(excel.getMgmtCost()),
                excel.getEstPrice() == 0 ? null : String.valueOf(excel.getEstPrice()),
                excel.getRefPrice() == 0 ? null : String.valueOf(excel.getRefPrice()),
                excel.getNote()
        };
    }

    public static String formatDateToLong(String inputDate) {
        if (inputDate == null) {
            return null;
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyMMdd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yy-MM-dd");
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            System.out.println("Util.formatDateToLong: " + e.getMessage());
            return inputDate;
        }
    }

    public static String formatDateToShort(String dateStr) {
        if (dateStr == null) {
            return null;
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyMMdd");
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            System.out.println("Util.formatDateToShort: " + e.getMessage());
            return dateStr;
        }
    }

    public int priceInputToInt(TextField tf) {
        try {
            if (tf != null) {
                String text = tf.getText();
                if (text != null && !text.trim().isEmpty()) {
                    return Integer.parseInt(text.trim().replace(",", ""));
                }
            }
        } catch (NumberFormatException e) {
            // optionally log: System.out.println("Invalid number input: " + tf.getText());
            System.out.println("Util.priceInputToInt: " + e.getMessage());
        }
        return 0;
    }

    public String bluePrintAndScan(TextField tf) {
        String text = tf.getText();
        if (text == null) {
            return null;
        }
        if (text.equals("0")) {
            return "○";
        } else {
            return text; // to take "X" as input
        }
    }
}
