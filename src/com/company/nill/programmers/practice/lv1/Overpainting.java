package com.company.nill.programmers.practice.lv1;

public class Overpainting {
    class Solution {
        public int solution(int n, int m, int[] section) {
            int answer = 0;
            int cover = 0;

            for (int i : section) {
                if (i > cover) {
                    answer++;
                    cover = i+m-1;
                }
            }

            return answer;
        }
    }

}
