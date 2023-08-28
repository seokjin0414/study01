package com.company.nill.test8to17;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;

public class BasicAndStaticMethod {

    public static void main(String[] args) {

        List<String> names = new ArrayList<>();
        names.add("shin");
        names.add("seok");
        names.add("jin");
        names.add("hi");
        names.add("there");


        Spliterator<String> spliterator = names.spliterator();
        Spliterator<String> spliterator1 = spliterator.trySplit();
        while (spliterator.tryAdvance(System.out::println));
        System.out.println("--------------------------------");
        while (spliterator1.tryAdvance(System.out::println));
        System.out.println("--------------------------------");


        long n = names.stream().map(String::toUpperCase)
                .filter(s -> s.startsWith("S"))
                .count();
        System.out.println(n);
        System.out.println("--------------------------------");


        names.removeIf(s -> s.startsWith("j"));
        names.forEach(System.out::println);
        System.out.println("--------------------------------");


        Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
        names.sort(compareToIgnoreCase.reversed());
        names.forEach(System.out::println);





    }

}
