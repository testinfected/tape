package com.vtence.tape.testmodel.builders;

public class FakeNumber {

    private static final int MAX_ITEM_NUMBER = 100000000;
    private final RandomNumber faker = new RandomNumber(MAX_ITEM_NUMBER);

    public String generate() {
        return faker.generate();
    }

    public static String aNumber() {
        return new FakeNumber().generate();
    }
}