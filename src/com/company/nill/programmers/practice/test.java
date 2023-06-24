package com.company.nill.programmers.practice;

// 코드 작성용
public class test {
    public static void main(String[] args) {
        test t = new test();


    }


    public String solution(String X, String Y) {
        StringBuilder sb = new StringBuilder();
        StringBuilder x = new StringBuilder(X);
        StringBuilder y = new StringBuilder(Y);
        int cnt = 0;

        for (int i = 0; i < x.length(); i++) {
            for (int j = 0; j < y.length(); j++) {
                if (x.charAt(i) == y.charAt(j)) {
                    if (y.charAt(j) != 0) {
                        cnt++;
                    }
                    sb.append(y.charAt(j));
                    y.deleteCharAt(j);
                    break;
                }
            }
        }

        if (sb.length() == 0) {
            return "-1";
        }

        if (cnt == 0) {
            return "0";
        }

        char[] ca = sb.toString().toCharArray();
        sb = new StringBuilder(String.valueOf(ca));
        sb = sb.reverse();

        return sb.toString();
    }


}
