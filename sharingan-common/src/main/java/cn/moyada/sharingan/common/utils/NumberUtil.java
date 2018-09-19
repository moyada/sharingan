package cn.moyada.sharingan.common.utils;

public class NumberUtil {

    public static int getIdempotent(int value) {
        int num = 1;

        do {
            value = value >> 1;
            num = num << 1;
        }while (value != 1);

        return num;
    }
}
