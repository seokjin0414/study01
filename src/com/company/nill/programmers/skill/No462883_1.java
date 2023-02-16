package com.company.nill.programmers.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class No462883_1 {
    public Integer[] solution(int[] numbers) {
        List<Integer> list = new ArrayList<>();

        for(int i=0; i<numbers.length; i++) {
            for(int j=i+1; j< numbers.length; j++) {
                int n = numbers[i] + numbers[j];

                list.add(n);

            }
        }

        List<Integer> newList = list.stream().distinct().collect(Collectors.toList());
        Collections.sort(newList);

        Integer[] answer = newList.stream().toArray(Integer[]::new);

        Arrays.sort(answer);

        return answer;
    }

}
