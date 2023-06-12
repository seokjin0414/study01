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
        answer[1] = a*b/c;


        return answer;
    }

    public int[] solution2(int n, int m) {
        int[] answer = new int[2];

        answer[0] = gcd(n,m);
        answer[1] = (n*m)/answer[0];
        return answer;
    }

    public static int gcd(int p, int q) {
        if (q == 0) return p;
        return gcd(q, p%q);
    }
}
