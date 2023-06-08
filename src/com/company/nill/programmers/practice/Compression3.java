package com.company.nill.programmers.practice;
import java.util.*;

public class Compression3 {
    class Solution {
        HashMap<String, Integer> m = new HashMap<>();
        public int[] solution(String msg) {
            int[] answer;
            ArrayList<Integer> list = new ArrayList<>();

            int dictIdx = 1;
            for(int i='A'; i<='Z'; i++){
                m.put( String.valueOf((char)i), dictIdx++) ;
            }

            int idx = 0;
            while(idx < msg.length()){
                String w = "";
                while(idx < msg.length()){
                    if(!m.containsKey(w+msg.charAt(idx))){
                        break;
                    }else{
                        w += msg.charAt(idx);
                    }
                    idx++;
                }

                list.add(m.get(w));
                if(idx < msg.length()){
                    m.put(w+msg.charAt(idx), dictIdx++);
                }
            }

            answer = new int[list.size()];
            for(int i =0; i<list.size(); i++){
                answer[i] = list.get(i);
            }

            return answer;
        }
    }

}
