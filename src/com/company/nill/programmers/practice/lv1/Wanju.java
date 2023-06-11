package com.company.nill.programmers.practice.lv1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Wanju {
    public String solution(String[] participant, String[] completion) {
        String answer = "";

        Arrays.sort(participant);
        Arrays.sort(completion);

        for (int i = 0; i < completion.length; i++) {
            if (!participant[i].equals(completion[i])) {
                answer = participant[i];
                break;
            }

            if (i == completion.length - 1) {
                answer = participant[i+1];
            }
        }

        return answer;
    }

    public String solution2(String[] participant, String[] completion) {
        String answer = "";
        HashMap<String, Integer> map = new HashMap<>();

        for (String player : participant)
            map.put(player, map.getOrDefault(player, 0) + 1);

        for (String player : completion)
            map.put(player, map.get(player) - 1);

       for (Map.Entry<String, Integer> entry : map.entrySet()) {
           if(entry.getValue() != 0) {
               answer = entry.getKey();
               break;
           }
        }

        return answer;
    }


}

