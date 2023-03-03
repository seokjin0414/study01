package com.company.nill.myTool.utill;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static com.withdraw.batch.comm.StringUtils.formatValue;

public class Calculator {

    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1, MathContext.UNLIMITED);
        BigDecimal b2 = new BigDecimal(v2, MathContext.UNLIMITED);
        return b1.add(b2).toPlainString();
    }

    public static String subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1, MathContext.UNLIMITED);
        BigDecimal b2 = new BigDecimal(v2, MathContext.UNLIMITED);
        return b1.subtract(b2).toPlainString();
    }

    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1, MathContext.UNLIMITED);
        BigDecimal b2 = new BigDecimal(v2, MathContext.UNLIMITED);
        return b1.multiply(b2).toPlainString();
    }

    public static String divide(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1, MathContext.UNLIMITED);
        BigDecimal b2 = new BigDecimal(v2, MathContext.UNLIMITED);
        MathContext mc = new MathContext(16, RoundingMode.DOWN);
        return b1.divide(b2, mc).toPlainString();
    }
    
    public static int compare(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1, MathContext.UNLIMITED);
        BigDecimal b2 = new BigDecimal(v2, MathContext.UNLIMITED);
        return b1.compareTo(b2);
    }

    public static String add(String v1, String v2, int scale) {
        return formatValue(add(v1, v2), scale);
    }

    public static String subtract(String v1, String v2, int scale) {
        return formatValue(subtract(v1, v2), scale);
    }

    public static String multiply(String v1, String v2, int scale) {
        return formatValue(multiply(v1, v2), scale);
    }

    public static String divide(String v1, String v2, int scale) {
        return formatValue(divide(v1, v2), scale);
    }

}
