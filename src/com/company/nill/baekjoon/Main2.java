package com.company.nill.baekjoon;

import java.io.*;

class Main2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int a = Integer.parseInt(br.readLine());
        int b = Integer.parseInt(br.readLine());

        bw.write(a+b);
        bw.write(a-b);
        bw.write(a*b);
        bw.write(a/b);
        bw.write(a%b);

        bw.flush();
        br.close();
        bw.close();
    }

}