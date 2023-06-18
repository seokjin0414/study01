package com.company.nill.programmers.practice.lv0;

import java.io.*;
import java.util.StringTokenizer;

public class PrintOutString {
    public class Solution {
        public static void main(String[] args) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

            bw.write(br.readLine().toString().replaceAll(" ", ""));

            br.close();
            bw.flush();
            bw.close();
        }
    }
}
