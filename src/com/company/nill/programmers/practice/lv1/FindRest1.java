package com.company.nill.programmers.practice.lv1;

public class FindRest1 {
    class Solution {
        public int solution(int n) {
            int answer = 1;
            while (true) {
                if (n % answer == 1) {
                        break;
                }
                answer++;
            }

            return answer;
        }
    }
}
