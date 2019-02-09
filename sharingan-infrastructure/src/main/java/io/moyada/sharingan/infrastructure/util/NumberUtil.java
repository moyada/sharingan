package io.moyada.sharingan.infrastructure.util;

public class NumberUtil {

    public static int getIdempotent(int value) {
        int num = 1;

        do {
            value = value >> 1;
            num = num << 1;
        }while (value != 1);

        return num;
    }

    public static Integer toInt(String input) {
        Integer value;
        try {
            value = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            value = null;
        }
        return value;
    }

    public static Double toDouble(String input) {
        Double value;
        try {
            value = Double.valueOf(input);
        } catch (NumberFormatException e) {
            value = null;
        }
        return value;
    }
}
