package com.company.nill;


public class Test_03 {
    public static void main(String[] args) {
        int[] a = new int[6];

        for (int i = 0; i < 6; i++) {
            a[i] = (int) (Math.random() * 100 + 1);
            for (int j = 0; j < i; j++)
                if (a[i] == a[j])
                    i--;
        }

        System.out.println("출력!!!!!");

        for (int k = 0; k < 6; k++)
            System.out.print(a[k] + " ");

    }

}
