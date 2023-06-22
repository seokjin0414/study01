package com.company.nill.programmers.practice.lv1;

// LV 1 - 옹알이 (2)
public class Babbling2 {
    class Solution {
        public static String[] list = {"aya", "ye", "woo", "ma"};
        public int solution(String[] babbling) {
            int answer = 0;

            for (String s : babbling) {
                answer += check(s);
            }

            return answer;
        }

        private int check(String babbling) {
            String a = babbling;
            String lastBabbling = "";

            while (true) {
                if (a.length() == 0) {
                    return 1;
                }

                int n = 0;
                for (String s : list) {
                    if (a.indexOf(s) == 0) {
                        if (s.equals(lastBabbling)) {
                            break;
                        }
                        a = a.substring(s.length());
                        lastBabbling = s;
                        n++;
                        break;
                    }
                }

                if (n == 0)  {
                    break;
                }
            }
            return 0;
        }
    }
}
