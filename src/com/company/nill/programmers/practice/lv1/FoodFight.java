package com.company.nill.programmers.practice.lv1;

public class FoodFight {
    class Solution {
        public String solution(int[] food) {
            StringBuffer sb = new StringBuffer();

            for (int i=1; i< food.length; i++) {
                sb.append(String.valueOf(i).repeat(food[i]/2));
            }

            StringBuffer sb2 = new StringBuffer(sb);
            sb.append("0");
            sb.append(sb2.reverse());

            return sb.toString();
        }
    }
}
