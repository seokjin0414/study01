package com.company.nill.test.clab;

import java.math.BigDecimal;

public class BigTest {

    public static void main(String[] args) {
        BigDecimal a = null;
        a = new BigDecimal("-1.23");
        BigDecimal b = new BigDecimal("123");
        BigDecimal c = new BigDecimal(0);
        c = new BigDecimal(123);
        BigDecimal d = BigDecimal.ZERO;

        int q = a.compareTo(new BigDecimal(0));
        int w = b.compareTo(new BigDecimal(0));
        int e = c.compareTo(new BigDecimal(0));

        d = d.add(b);


        System.out.println(q);
        System.out.println(w);
        System.out.println(e);

        System.out.println(a);
        System.out.println(d);

    }

}
