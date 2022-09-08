package com.company.nill.test;

public class Main {

    public static void main(String[] args) {
        int[][] a = {
                {95, 86},
                {83, 92, 96},
                {78, 83, 93, 87, 88}
        };

        int sum = 0;
        double avg = 0.0;
        int p = 0;

        for (int i = 0; i < a.length; i++) {
            for (int b : a[i]) {
                sum += b;
            }
            p += a[i].length;
        }

        avg = sum / p;

        System.out.println("sum = " + sum);
        System.out.println("avg = " + avg);
    }

}
