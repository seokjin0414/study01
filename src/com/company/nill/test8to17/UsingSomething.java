package com.company.nill.test8to17;

public class UsingSomething {

    public static void main(String[] args) {
        //  익명 내부 class, anonymous inner class
        /*RunSomething runSomething = new RunSomething() {
            @Override
            public void doIt() {

            }
        };
*/

        RunSomething runSomething = () -> {
            System.out.println("LLL");
        };
        runSomething.doIt();

    }

}
