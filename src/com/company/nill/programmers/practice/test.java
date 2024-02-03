package com.company.nill.programmers.practice;


import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

// 코드 작성용
public class test {
    public static void main(String[] args) throws IOException {
       /* BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        bw.write(br.readLine());
        bw.flush();
        br.close();
        bw.close();*/
    }

    class Solution {
        static boolean[] v;
        static int c = 0;

        public int solution(int k, int[][] dungeons) {
            v = new boolean[dungeons.length];
            f(0, k, dungeons);
            return c;
        }

        private void f(int d, int cd, int[][] dungeons) {
            for (int i = 0; i < dungeons.length; i++) {
                if (v[i] || dungeons[i][0] > cd) {
                    continue;
                }
                v[i] = true;
                f(d + 1, cd - dungeons[i][1], dungeons);
                v[i] = false;
            }
            c = Math.max(c, d);
        }
    }

    public int[] plusOne(int[] digits) {
        int carry = 1;
        int index = digits.length - 1;

        while (index >= 0 && carry > 0) {
            digits[index] = (digits[index] + 1) % 10;
            carry = (digits[index] == 0) ? 1 : 0;

            index--;
        }

        if (carry > 0) {
            digits = new int[digits.length + 1];
            digits[0] = 1;
        }

        return digits;
    }

    public int pivotIndex(int[] nums) {
        int r = 0;
        int l = 0;
        for (int n : nums) {
            r += n;
        }
        for (int i = 0; i < nums.length; l += nums[i++]) {
            if (l * 2 == r - nums[i]) {
                return i;
            }
        }
        return -1;
    }

    public int f(int a, int b, int c) {
        int n = 0;
        int min = Math.min(a, b) % c == 0 ? Math.min(a, b) / c : Math.min(a, b) / c + 1 ;
        int max = Math.max(a, b) / c;
        for (int i = min; i <= max; i++) {
            n += i * c;
        }
        return n;
    }
}