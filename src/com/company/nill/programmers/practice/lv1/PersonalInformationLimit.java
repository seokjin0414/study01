package com.company.nill.programmers.practice.lv1;

import java.util.*;

public class PersonalInformationLimit {
    class Solution {
        public Object solution(String today, String[] terms, String[] privacies) {
            List<Integer> answer = new ArrayList<>();
            Map<String,Integer> map = new HashMap<>();
            int t = getDay(today);

            for (String s : terms) {
                map.put(s.split(" ")[0], Integer.parseInt(s.split(" ")[1]) * 28);
            }

            for (int i=0; i<privacies.length; i++) {
                String[] a = privacies[i].split(" ");
                 if (t >= getDay(a[0]) + (map.get(a[1]))) {
                     answer.add(i+1);
                 }
            }
            return answer;
        }

        private int getDay(String date) {
            String[] vo = date.split("\\.");
            int y = Integer.parseInt(vo[0]);
            int m = Integer.parseInt(vo[1]);
            int d = Integer.parseInt(vo[2]);
            return (y * 12 * 28) + (m * 28) + d;
        }

    }
}
