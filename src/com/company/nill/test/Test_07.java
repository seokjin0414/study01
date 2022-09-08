package com.company.nill.test;

public class Test_07 {

    public static void main(String[] args) {

        for (int i = 1; i <= 9; i++) {
            if (i <= 5) {
                for (int j = 1; j <= i - 1; j++)
                    System.out.print("o");
                System.out.print("O");
                for (int j = 1; j <= 5 - i; j++)
                    System.out.print("o");
                System.out.println();
            } else {
                for (int k = 1; k <= 9 - i; k++)
                    System.out.print("o");
                System.out.print("O");
                for (int k = 1; k <= i - 5; k++)
                    System.out.print("o");
                System.out.println();
            }
        }

    }

}
