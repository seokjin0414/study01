package com.company.nill.programmers.practice;

// 코드 작성용
public class test {
    public static void main(String[] args) {
        test t =new test();
        t.solution("a", 1);

    }

    public String solution(String s, int n) {
        String lw = "abcdefghijklmnopqrstuvwxyz";
        String up = lw.toUpperCase();

        StringBuffer sb = new StringBuffer();

        for (int i=0; i<s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) {
                if (Character.isUpperCase(s.charAt(i))) {
                    sb.append(up.charAt((up.indexOf(s.charAt(i)) + n) % up.length()));
                } else {
                    sb.append(lw.charAt((lw.indexOf(s.charAt(i)) + n) % lw.length()));
                }
            } else {
                sb.append(" ");
            }
        }

        return sb.toString();
    }






}
