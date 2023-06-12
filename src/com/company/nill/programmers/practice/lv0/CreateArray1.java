package com.company.nill.programmers.practice.lv0;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreateArray1 {
    public Object solution(int n, int k) {
        List<Integer> answer = new ArrayList<>();

        for(int i=1;i<=n; i++) {
            if (i%k == 0) answer.add(i);
        }

        answer.sort(Comparator.naturalOrder());

        return answer;
    }
}
