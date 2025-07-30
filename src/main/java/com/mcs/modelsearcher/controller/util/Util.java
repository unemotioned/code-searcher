package com.mcs.modelsearcher.controller.util;

import javafx.scene.control.TextField;

public class Util {
    public int priceInputToInt(TextField tf) {
        try {
            if (tf != null && tf.getText() != null) {
                return Integer.parseInt(tf.getText().trim().replace(",", ""));
            }
        } catch (NumberFormatException e) {
            System.out.println("Util.priceToInt: " + e.getMessage());
        }
        return 0;
    }
}
