package com.company.nill.programmers.practice.lv1;

public class DivideString {
    public int solution(String s) {
        int answer = 0;

        char x = s.charAt(0);
        int a = 0;
        int b = 0;

        for (int i=0; i<s.length(); i++) {
            if (a==b) {
                answer++;
                x = s.charAt(i);
            }
            if (x==s.charAt(i)) {
                a++;
            } else {
                b++;
            }
        }
        return answer;
    }
}
