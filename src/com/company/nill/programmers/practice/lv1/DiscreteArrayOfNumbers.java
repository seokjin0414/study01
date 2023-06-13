package com.company.nill.programmers.practice.lv1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DiscreteArrayOfNumbers {
    public Object solution(int[] arr, int divisor) {
        List<Integer> answer = new ArrayList<>();

        for (int n : arr) {
            if (n%divisor == 0) {
                answer.add(n);
            }
        }

        if (answer.size()>0) {
            answer.sort(Comparator.naturalOrder());
        } else {
            answer.add(-1);
        }

        return answer;
    }
}
