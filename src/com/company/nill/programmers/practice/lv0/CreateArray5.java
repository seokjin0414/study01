package com.company.nill.programmers.practice.lv0;

import java.util.ArrayList;
import java.util.List;

public class CreateArray5 {
    class Solution {
        public Object solution(String[] intStrs, int k, int s, int l) {
            List<Integer> answer = new ArrayList<>();

            for (String x : intStrs) {
                int y = Integer.parseInt(x.substring(s,s+l));
                if (y > k) {
                    answer.add(y);
                }
            }

            return answer;
        }
    }
}
