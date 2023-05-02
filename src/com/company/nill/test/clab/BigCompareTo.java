package com.company.nill.test.clab;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigCompareTo {
    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("-1.23");
        BigDecimal b = new BigDecimal("1");
        BigDecimal c = BigDecimal.ZERO;



        int q = a.compareTo(b);
        int w = a.compareTo(c);


        System.out.println(q);
        System.out.println(w);

        System.out.println(a);

    }

}
