package com.company.nill.programmers.practice.lv1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// LV 1 - 명예의 전당 (1)
public class HallOfFame1 {
    class Solution {
        public Object solution(int k, int[] score) {
            List<Integer> answer = new ArrayList<>();
            List<Integer> list = new ArrayList<>();
            for (int i : score) {
                if (list.size() >= k) {
                    if (i > list.get(0)) {
                        list.set(0,i);
                    }
                } else {
                    list.add(i);
                }

                list.sort(Comparator.naturalOrder());
                answer.add(list.get(0));
            }
            return answer;
        }
    }
}
