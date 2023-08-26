package com.company.nill.test8to17;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class Foo {
    public static void main(String[] args) {
            Foo foo = new Foo();
            foo.run();
    }

    public void run() {
        final int baseNum = 10;

        //  로컬 클래스
        class LocalClass {
            int baseNum = 11;
            void printBaseNum() {
                System.out.println(baseNum);
            }
        }

        //  익명 클래스
        Consumer<Integer> integerConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer baseNum) {
                System.out.println(baseNum);
            }
        };


        //  람다
        IntConsumer printInt = (i) -> {
            System.out.println(i + baseNum);
        };
        printInt.accept(100);
    }

}
