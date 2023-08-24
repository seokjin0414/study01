package com.company.nill.test8to17;

import java.util.function.Function;

public class UsingSomething {

    public static void main(String[] args) {
        //  익명 내부 class, anonymous inner class
        /*RunSomething runSomething = new RunSomething() {
            @Override
            public void doIt() {

            }
        };
*/
        int baseNumber = 10;
        RunSomething runSomething = number -> number + baseNumber;

        Plus10 plus10 = new Plus10();
        System.out.println(plus10.apply(1));


        Function<Integer, Integer> plus20 = (i) -> i + 20;
        Function<Integer, Integer> multiply2 = (i) -> i * 2;
        Function<Integer, Integer> multiply2AndPlus20 = plus20.compose(multiply2);

        System.out.println(plus20.apply(2));
        System.out.println(multiply2.apply(2));
        System.out.println(multiply2AndPlus20.apply(6));




    }

}
