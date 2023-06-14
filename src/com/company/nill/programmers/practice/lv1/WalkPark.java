package com.company.nill.programmers.practice.lv1;

public class WalkPark {
    class Solution {
        public int[] solution(String[] park, String[] routes) {
            int[] answer = new int[2];
            int x = 0;
            int y = 0;
            int h = park.length - 1;
            int w = park[0].length() - 1;

            for (int i = 0; i < h + 1; i++) {
                if (park[i].indexOf("S") != -1) {
                    x = park[i].indexOf("S");
                    y = i;
                    break;
                }
            }

            for (String s : routes) {
                int myX = x;
                int myY = y;
                String d = s.split(" ")[0];
                int n = Integer.parseInt(s.split(" ")[1]);

                if (d.equals("E")) {
                    myX += n;
                } else if (d.equals("W")) {
                    myX -= n;
                } else if (d.equals("S")) {
                    myY += n;
                } else {
                    myY -= n;
                }

                if (myX > w || myY > h || myX < 0 || myY < 0) {
                    continue;
                }

                if (myX != x) {
                    if (park[myY].substring(Math.min(myX, x), Math.max(myX, x) + 1).indexOf("X") != -1) {
                        continue;
                    } else {
                        x = myX;
                    }
                } else {
                    int t = 0;

                    for (int i = Math.min(myY, y); i <= Math.max(myY, y); i++) {
                        if (park[i].substring(myX, myX + 1).equals("X")) {
                            t++;
                            break;
                        }
                    }

                    if (t == 0) {
                        y = myY;
                    }
                }
            }

            answer[0] = y;
            answer[1] = x;
            return answer;
        }
    }
}
