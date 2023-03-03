package com.company.nill.myTool.utill;

import java.math.BigDecimal;

public class DecimalUtils {

    public static String getDouble(String val) {
        return new BigDecimal(Double.parseDouble(val)).setScale(8).toPlainString();
    }


    public static String getDouble(double val) {
        return new BigDecimal(val).setScale(8).toPlainString();
    }

    public static int compare(String source, String target) {
        BigDecimal sourceDecimal = new BigDecimal(source);
        BigDecimal targetDecimal = new BigDecimal(target);
        return sourceDecimal.compareTo(targetDecimal);
    }
}
