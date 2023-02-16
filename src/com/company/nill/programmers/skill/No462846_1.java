package com.company.nill.programmers.skill;


public class No462846_1 {



        public String solution(String s) {
            String answer = "";
            int a = s.length() / 2;


            if (s.length() % 2 == 0) {
                answer = s.substring(a-1, a);
            } else {
                answer = s.substring(a, a+1);
            }



            return answer;
        }


}
