package com.company.nill.programmers.practice.LV2;

public class BinaryTransformation {
    class Solution {
        public int[] solution(String s) {
            int[] answer = new int[2];

            while(s.length() > 1) {
                int c = 0;

                for(int i=0; i<s.length(); i++) {
                    if(s.charAt(i) == '0') {
                        answer[1]++;
                    } else {
                        c++;
                    }
                }

                s = Integer.toBinaryString(c);
                answer[0]++;
            }
            return answer;
        }
    }
}
