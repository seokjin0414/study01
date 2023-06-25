package com.company.nill.programmers.practice.lv1;

// LV 1 - 숫자 짝꿍
public class NumberPartner {
    public String solution(String X, String Y) {
        StringBuilder sb = new StringBuilder();

        int[] xArr = new int[10];
        int[] yArr = new int[10];

        for (String s : X.split("")) {
            xArr[Integer.parseInt(s)]++;
        }
        for (String s : Y.split("")) {
            yArr[Integer.parseInt(s)]++;
        }

        for (int i=xArr.length-1; i>=0; i--) {
            int n = Math.min(xArr[i],yArr[i]);

            while (n>0) {
                n--;
                sb.append(i);
            }
        }

        if (sb.length() == 0) {
            return "-1";
        } else if (sb.charAt(0) == '0') {
            return "0";
        }


        return sb.toString();
    }

    public String solution2(String X, String Y) {
        StringBuilder answer = new StringBuilder();
        int[] x = {0,0,0,0,0,0,0,0,0,0};
        int[] y = {0,0,0,0,0,0,0,0,0,0};
        for(int i=0; i<X.length();i++){
            x[X.charAt(i)-48] += 1;
        }
        for(int i=0; i<Y.length();i++){
            y[Y.charAt(i)-48] += 1;
        }

        for(int i=9; i >= 0; i--){
            for(int j=0; j<Math.min(x[i],y[i]); j++){
                answer.append(i);
            }
        }
        if("".equals(answer.toString())){
            return "-1";
        }else if(answer.toString().charAt(0)==48){
            return "0";
        }else {
            return answer.toString();
        }
    }

}
