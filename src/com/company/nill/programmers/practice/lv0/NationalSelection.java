package com.company.nill.programmers.practice.lv0;

import java.util.*;

public class NationalSelection {
    class Solution {
        public int solution(int[] rank, boolean[] attendance) {
            Map<Integer, Integer> map = new HashMap<>();

            for (int i=0; i<rank.length; i++) {
                if(attendance[i]) {
                    map.put(rank[i], i);
                }
            }

            List<Integer> rankList = new ArrayList<>(map.keySet());
            rankList.sort(Comparator.naturalOrder());

            int answer = (10000 * map.get(rankList.get(0))) + (100 * map.get(rankList.get(1))) + map.get(rankList.get(2));

            return answer;
        }
    }

}
