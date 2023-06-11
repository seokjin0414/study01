package com.company.nill.programmers.practice.lv0;

import java.util.*;

public class SpecialSort {
    class Solution {
        public Object[] solution(int[] numlist, int n) {
            ArrayList<Integer> answer = new ArrayList<Integer>();

            for(int i=0; i<numlist.length; i++) {
                answer.add(numlist[i]);
            }

            Collections.sort(answer, new Comparator<Integer>() {
                @Override
                public int compare(Integer a, Integer b) {
                    if(Math.abs(a-n)!=Math.abs(b-n)) {
                        return Math.abs(a-n)-Math.abs(b-n);
                    } else {
                        return b-a;
                    }
                }
            });
            return answer.toArray();
        }
    }

}
