package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Test_01 {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        System.out.print("a값을 입력하시오");
        int a = kb.nextInt();
        System.out.print("b값을 입력하시오");
        int b = kb.nextInt();
        System.out.print("c값을 입력하시오");
        int c = kb.nextInt();

        // int n = (b / c) - (a / c) - 1;

        ArrayList<Integer> val = new ArrayList<>();


        for (int j = a + 1; j < b; j++)
            if (j % c == 0)
                val.add(j);

        for (int n : val)
            System.out.print(n + " ");
        System.out.println();

        int total = 1;

        for (int i = 0; i < val.size(); i++)
            total *= val.get(i);

        System.out.print(a + "와 " + b + "사이의 " + c + "의 배수의 곱: " + total);


    }
}