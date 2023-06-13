package com.company.nill.programmers.practice.lv0;

import java.util.ArrayList;
import java.util.List;

public class ElementN {
    class Solution {
        public List<Integer> solution(int[] num_list, int n) {
            List<Integer> answer = new ArrayList<>();

            for (int i=0; i<n; i++) {
                answer.add(num_list[i]);
            }

            return answer;
        }
    }
}
