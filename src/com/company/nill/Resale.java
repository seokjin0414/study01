package com.company.nill;

import java.util.ArrayList;
import java.util.List;

public class Resale {
    public static void main(String[] args) {
        List<Integer> s = new ArrayList<Integer>();

                for (int i=1; i<=100; i++) {
                    s.add(i);
                }
        System.out.println(s.size());

        List<List<Integer>> r = new ArrayList<>();

        for(int j=0 ; j < s.size(); j+=4) {
            List<Integer> a = new ArrayList<>();
            a.add(s.get(j));
            a.add(s.get(j+1));
            a.add(s.get(j+2));
            a.add(s.get(j+3));
            System.out.println(a.size());
            r.add(a);
        }
        System.out.println(r.size());

        for (int e=0; e<r.size(); e++) {
            System.out.println(r.get(e));
        }
        System.out.println(r.get(0).get(3));


    }
}
