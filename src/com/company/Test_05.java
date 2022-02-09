package com.company;

import java.util.Scanner;

public class Test_05 {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        System.out.print("전화번호를 입력하세요: ");
        String a = kb.next();

        System.out.println(a + " -> " + form(a));

    }

    static String form(String a) {
        String b = "";

        if(a.length() == 11) {
            b = a.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        } else if(a.length() == 10) {
            if(a.startsWith("02")){
                b = a.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "$1-$2-$3");
            } else
                b = a.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        } else if(a.length() == 9) {
            b = a.replaceAll("(\\d{2})(\\d{3})(\\d{4})", "$1-$2-$3");
        } else  if(a.length() == 8) {
            b = a.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
        } else
            b = a;
        return b;
    }

}
