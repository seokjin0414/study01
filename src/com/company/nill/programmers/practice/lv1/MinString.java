package com.company.nill.programmers.practice.lv1;

public class MinString {
    class Solution {
        public int solution(String t, String p) {
            int answer = 0;
            long intP = Long.parseLong(p);
            int l = p.length();

            for (int i = 0; i <= t.length() - l; i++) {

                if (intP >= Long.parseLong(t.substring(i, i + l))) {
                    answer++;
                }
            }
            return answer;
        }
    }
}
