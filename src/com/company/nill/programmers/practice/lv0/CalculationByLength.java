package com.company.nill.programmers.practice.lv0;

// LV 0 - 길이에 따른 연산
public class CalculationByLength {
    class Solution {
        public int solution(int[] num_list) {
            int answer = 0;

            if (num_list.length > 10) {
                for (int i : num_list) {
                    answer+=i;
                }
            } else {
                answer = 1;
                for (int j : num_list) {
                    answer*=j;
                }
            }

            return answer;
        }
    }
}
