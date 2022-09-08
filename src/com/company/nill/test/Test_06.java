package com.company.nill.test;

import com.sun.beans.decoder.ValueObject;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Scanner;

public class Test_06 {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        System.out.println("HHMMSS 을 입력하세요: ");
        String a = kb.next();

        String b = a.substring(0,2) + ":" + a.substring(2,4) + ":" + a.substring(4,6);
        System.out.println(b);

        System.out.println("더할시간_초 를 입력하세요: ");
        int s = kb.nextInt();

        SimpleDateFormat newDtFormat = new SimpleDateFormat("HH:mm:ss");

        LocalTime t = LocalTime.parse(b);
        System.out.println(t.plusSeconds(s));

        System.out.println("~~~~~~~~~");
        LocalTime t1 = t.plusSeconds(s);

        System.out.println(t1);

        // String c = (String) ValueObject(t1);

        System.out.println("");
    }
}
