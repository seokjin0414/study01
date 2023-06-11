package com.company.nill.programmers.practice.lv1;

import java.util.*;
import java.util.stream.Collectors;

public class ReturnResultOfreport {

    public Object solution(String[] id_list, String[] report, int k) {
        List<String> newReport = Arrays.stream(report).distinct().collect(Collectors.toList());
        List<Integer> answer = new ArrayList<>();
        LinkedHashMap<String, Integer> answerToMap = new LinkedHashMap();
        HashMap<String, List<String>> reportToMap = new HashMap();

        for (String i : id_list) {
            answerToMap.put(i, 0);
        }

        for (String j : newReport) {
            String[] splitOfReport = j.split(" ");
            List<String> list = new ArrayList<>();
            if (reportToMap.get(splitOfReport[1]) != null) {
                list = reportToMap.get(splitOfReport[1]);
            }
            list.add(splitOfReport[0]);
            reportToMap.put(splitOfReport[1], list);
        }

        reportToMap.forEach((key, value) -> {
            if (value.size() >= k) {
                for (String s : value) {
                    answerToMap.put(s, answerToMap.get(s) + 1);
                }
            }
        });

        answerToMap.forEach((key, value) -> {
            answer.add(value);
        });

        return answer;
    }

}
