package com.company.nill.programmers.practice.lv1;

import java.util.*;

public class SortStringIWant {
    class Solution {
        public Object solution(String[] strings, int n) {
            List<String> list = new ArrayList<>();
            List<String> answer = new ArrayList<>();

            for (String s : strings) {
                list.add(s.substring(n,n+1)+s);
            }

            list.sort(Comparator.naturalOrder());

            for (String s : list) {
                answer.add(s.substring(1));
            }

            return answer;
        }
    }
}
