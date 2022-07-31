package com.company.nill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SetHistory {
    public static void main(String[] args) throws ParseException {
        // ["2022년 7월 31일", "오후 1:16", "8.5", "US$189"];
        // ["2022년 7월 01일", "오후 10:16", "8.5", "US$18"];
        // ["2022년 10월 31일", "오후 12:16", "7.0", "US$77"];
        // ["2022년 10월 01일", "오전 10:16", "8.5", "US$109"]
        // ["2022년 5월 11일", "오전 11:06", "8.5", "US$109"]
        // ["2022년 10월 20일", "오전 12:16", "10.5", "US$1000"]
        // ["2022년 10월 24일", "오전 2:16", "8.5", "US$1000"]
        List<String> a1 = new ArrayList<>(Arrays.asList("2022년 7월 31일", "오후 1:16", "8.5", "US$189"));
        List<String> a2 = new ArrayList<>(Arrays.asList("2022년 7월 01일", "오후 10:16", "8.5", "US$1894"));
        List<String> a3 = new ArrayList<>(Arrays.asList("2022년 10월 31일", "오후 12:16", "7.0", "US$77"));
        List<String> a4 = new ArrayList<>(Arrays.asList("2022년 10월 01일", "오전 10:16", "8.5", "US$109"));
        List<String> a5 = new ArrayList<>(Arrays.asList("2022년 5월 11일", "오전 11:06", "8.5", "US$569"));
        List<String> a6 = new ArrayList<>(Arrays.asList("2022년 10월 20일", "오전 12:16", "10.5", "US$1000"));
        List<String> a7 = new ArrayList<>(Arrays.asList("2022년 10월 24일", "오전 2:16", "8.5", "US$200"));

        List<List<String>> history = new ArrayList<>();
        history.add(a1);
        history.add(a2);
        history.add(a3);
        history.add(a4);
        history.add(a5);
        history.add(a6);
        history.add(a7);

        for (List<String> e : history) {
            System.out.println(e);
        }

        for (List<String> e : history) {
            String t1 = e.get(0);
            System.out.println("@@@@@@ t1 = (" + t1 + ")");
            String t2 = e.get(1);
            if (t2.substring(0, 2).equals("오전")) {
                t2 = t2.substring(3);
            } else {
                if ((t2.length() == 8)) {
                    String a = String.valueOf(12 + Integer.parseInt(t2.substring(3, 5)));
                    t2 = a + t2.substring(5, 8);
                } else {
                    String a = String.valueOf(12 + Integer.parseInt(t2.substring(3, 4)));
                    t2 = a + t2.substring(4, 7);
                }
            }
            System.out.println("@@@@@@ t2 = (" + t2 + ")");
            String t3 = t1 + " " + t2;
            System.out.println("@@@@@@ t3 = (" + t3 + ")");
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
            Date date = format.parse(t3);
            System.out.println("@@@@@@ date = (" + date + ")");
//            System.out.println("@@@@@@ ShoesSize = (" + e.get(2) + ")");
//            System.out.println("@@@@@@ Price = (" + e.get(3).substring(3) + ")");
        }

    }
}
