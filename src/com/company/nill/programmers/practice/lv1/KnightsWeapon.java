package com.company.nill.programmers.practice.lv1;

public class KnightsWeapon {
    public int solution(int number, int limit, int power) {
            int answer = 0;

            for (int i=1; i<=number; i++) {
                int maxP = 0;
                for (int j=1; j*j<=i; j++) {
                    if (j*j == i) {
                        maxP++;
                    } else if (i%j == 0) {
                        maxP+=2;
                    }
                }

                if (maxP > limit) {
                    maxP = power;
                }

                answer+=maxP;
            }

            return answer;
    }
}
