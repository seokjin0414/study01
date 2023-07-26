package com.company.nill.programmers.practice;


import java.util.Map;
import java.util.PriorityQueue;

// 코드 작성용
public class test {
    public static void main(String[] args) {
        test t = new test();
        int[][] ba = new int[][]{{0, 0, 0, 0, 0}, {0, 0, 1, 0, 3}, {0, 2, 5, 0, 1}, {4, 2, 4, 4, 2}, {3, 5, 1, 3, 1}};
        int[] ma = new int[]{1, 5, 3, 5, 1, 2, 1, 4};

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




}
