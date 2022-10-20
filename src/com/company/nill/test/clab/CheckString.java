package com.company.nill.test.clab;

import java.util.Stack;

public class CheckString {

    public static void main(String[] args) {

        String m = "-123.123";
        String n = null;
        String b = "";
        String b2 = "    ";
        String z = "0";
        String w = "asd";

            String a = "";
        try {
            if (Double.parseDouble(a) < 0) {
                System.out.println("유효");
            }
        } catch (NullPointerException nu){
            a = "0";
            System.out.println("null");
        } catch (NumberFormatException nm) {
            System.out.println("유효하지 아ㄴㅁㄴㅇ");
        }

        System.out.println(a);
    }

}
