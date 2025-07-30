package com.mcs.modelsearcher.controller.util;

import javafx.scene.control.TextField;

public class Util {
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
