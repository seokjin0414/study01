package com.company.nill.programmers.practice.lv1;

import java.util.ArrayList;
import java.util.List;

public class MockTest {
    public Object solution(int[] answers) {
        List<Integer> answer = new ArrayList<>();

        int[] a = {1,2,3,4,5};
        int[] b = {2, 1, 2, 3, 2, 4, 2, 5};
        int[] c = {3, 3, 1, 1, 2, 2, 4, 4, 5, 5};

        List<Integer> s = new ArrayList<>();
        s.add(0);
        s.add(0);
        s.add(0);

        int n = 0;
        int m = 0;

        for (int i: answers) {
            if (a[n%a.length] == i) {
                s.set(0, s.get(0)+1);
            }
            if (b[n%b.length] == i) {
                s.set(1, s.get(1)+1);
            }
            if (c[n%c.length] == i) {
                s.set(2, s.get(2)+1);
            }
            n++;
        }

        for (Integer i : s) {
            if (i > m) {
                m =i;
            }
        }

        for (int i=0; i<s.size(); i++) {
            if (s.get(i) == m) {
                answer.add(i+1);
            }
        }

        return answer;
    }
}
