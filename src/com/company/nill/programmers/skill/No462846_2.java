package com.company.nill.programmers.skill;

public class No462846_2 {

    class Solution {
        public int solution(int[] ingredient) {
            int answer = 0;

            for (int a = 0; a < ingredient.length; a++) {
                if (ingredient[a] == 1) {

                    if (ingredient[a+1] == 2) {

                        if (ingredient[a+2] == 3) {
                            if (ingredient[a+3] == 1) {
                                answer += 1;
                                a += 3;
                            } else {
                                a += 2;
                            }
                        } else {
                            a += 1;
                        }

                    }



                }
            }



            return answer;
        }
    }

}
