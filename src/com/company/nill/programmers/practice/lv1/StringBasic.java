package com.company.nill.programmers.practice.lv1;

// LV 1 - 문자열 다루기 기본
public class StringBasic {
    class Solution {
        public boolean solution(String s) {
            boolean answer = false;

            if (s.length()==4 || s.length()==6) {
                try {
                    Integer.parseInt(s);
                    answer = true;
                } catch (Exception e) {
                }
            }

            return answer;
        }
    }
}
