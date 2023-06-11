package com.company.nill.programmers.practice.lv0;

public class ImportA {
    class Solution {
        public String solution(String myString) {
            String answer = myString.toLowerCase();

            answer = answer.replaceAll("a", "A");


            return answer;
        }
    }
}
