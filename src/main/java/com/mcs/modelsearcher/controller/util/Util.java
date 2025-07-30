package com.mcs.modelsearcher.controller.util;

import javafx.scene.control.TextField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String formatDateToLong(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyMMdd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yy-MM-dd");
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            System.out.println(".formatDateToLong: " + e.getMessage());
            return inputDate;
        }
    }

    public static String formatDateToShort(String dateStr) {
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
}
