package com.company.nill.programmers.practice.lv1;

public class MaximumAndMinmumDivisor {
    public int[] solution(int n, int m) {
        int[] answer = new int[2];
        int a = 0;
        int b = 0;
        int c = 0;

        if (n >= m) {
            a = n;
            b = m;
        } else {
            a = m;
            b = n;
        }

        for (int i=1; i<=b;i++) {
            if(b%i==0 && a%i==0) {
                c = i;
            }
        }

        answer[0] = c;

        if (a % b == 0) {
            answer[1] = a;
        } else {
            answer[1] = a*b/c;
        }

        return answer;
    }
}
