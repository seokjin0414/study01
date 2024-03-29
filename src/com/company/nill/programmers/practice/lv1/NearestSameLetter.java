package com.company.nill.programmers.practice.lv1;

import java.util.ArrayList;
import java.util.List;

// LV 1 - 가장 가까운 같은 글자
public class NearestSameLetter {
    class Solution {
        public Object solution(String s) {
            List<Integer> answer = new ArrayList<>();

            for (int i = 0; i < s.length(); i++) {
                answer.add(-1);
                if (i > 0) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (s.charAt(i) == s.charAt(j)) {
                            answer.set(i ,i-j);
                            break;
                        }
                    }
                }
            }

            return answer;
        }
    }

}
