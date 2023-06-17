package com.company.nill.programmers.practice.lv1;

import java.util.ArrayList;
import java.util.List;

public class HateSameNumber {
    public class Solution {
        public Object solution(int []arr) {
            List<Integer> answer = new ArrayList<>();

            for (int i=0; i<arr.length; i++) {
                answer.add(arr[i]);
                for (int j=i+1; j<arr.length; j++) {
                    if (arr[i] == arr[j]) {
                        i++;
                    } else {
                        break;
                    }
                }
            }

            return answer;
        }
    }
}
