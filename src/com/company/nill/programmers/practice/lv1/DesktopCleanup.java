package com.company.nill.programmers.practice.lv1;

import java.util.ArrayList;
import java.util.List;

public class DesktopCleanup {
    class Solution {
        public Object solution(String[] wallpaper) throws Exception{
            List<Integer> answer = new ArrayList<>();
            int maxH = wallpaper.length;
            int minH = 0;
            int maxW = 0;
            int minW = wallpaper[0].length();

            for (int i=0; i< wallpaper.length; i++) {
                if (wallpaper[i].contains("#")) {
                    if (maxH >=i) {
                        maxH = i;
                    }
                    minH = i;

                    if (minW > wallpaper[i].indexOf("#")) {
                        minW = wallpaper[i].indexOf("#");
                    }

                    if(maxW < wallpaper[i].lastIndexOf("#")) {
                        maxW = wallpaper[i].lastIndexOf("#");
                    }
                }
            }

            answer.add(maxH);
            answer.add(minW);
            answer.add(minH+1);
            answer.add(maxW+1);

            return answer;
        }
    }


}
