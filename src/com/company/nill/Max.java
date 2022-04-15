package com.company.nill;

import java.util.Scanner;

public class Max {

    public static void main(String[] args) {

        Scanner kb = new Scanner(System.in);


        System.out.print("배열의 길이를 입력하세요 : ");
        int s = kb.nextInt();
        int[] n = new int[s];

        System.out.printf("정수 %d개를 입력하십시오: ", s);

        for (int i = 0; i < n.length; i++) {
            n[i] = kb.nextInt();

        }


        System.out.printf("가장 큰 수는 %d입니다.", getMax(n));
    }

    static int getMax(int[] n) {

        int max = n[0];

        for (int i = 1; i < n.length; i++) {
            max = n[i] > max ? n[i] : max;
        }

        return max;
    }
}
