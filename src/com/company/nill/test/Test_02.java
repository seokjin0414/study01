package com.company.nill.test;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Test_02 {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        DecimalFormat form = new DecimalFormat("###,###");

        System.out.print("금액을 입력하세요: ");
        Long n = kb.nextLong();

        System.out.println(n + " ==> " + form.format(n));

    }
}
