package com.company.nill.programmers.practice;


import java.io.*;
import java.util.Scanner;

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

}
