package com.company.nill.test;

public class asasas {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();

        for (int i = 1; i < 10; i++) {
            if (i % 4 != 0) {
                for (int j = 2; j < 10; j++) {
                    if (j < 6) {
                        sb.append(j + "x" + i + "=" + j * i + " ");
                    } else {
                        sb2.append(j + "x" + i + "=" + j * i + " ");
                    }
                }
                sb.append("\n");
                sb2.append("\n");
            }
        }
        System.out.println(sb.append(sb2));
    }
}

