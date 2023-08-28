package com.company.nill.test8to17;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class UsingGreeting {

    public static void main(String[] args) {
        UnaryOperator<String> hi = Greeting::hi;

        Greeting greeting = new Greeting();
        UnaryOperator<String> hello = greeting::hello;
        System.out.println(hello.apply("SHIN"));

        Supplier<Greeting> newGrreting = Greeting::new;
        newGrreting.get();

        Function<String, Greeting> shinGreeting = Greeting::new;
        System.out.println(shinGreeting.apply("JIN").getName());

        String[] names = {"SHIN", "SEOK", "JIN"};
        Arrays.sort(names, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(names));

    }

}
