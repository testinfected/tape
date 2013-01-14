package com.vtence.tape.testmodel;

import static java.lang.String.valueOf;

public class OrderNumber {

    private final String number;

    public OrderNumber(String number) {
        this.number = number;
    }

    public OrderNumber(long number) {
        this(leftPad(valueOf(number), 8, '0'));
    }

    private static String leftPad(String text, int size, char padding) {
        if (size <= text.length()) return text;

        final char[] buf = new char[size - text.length()];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padding;
        }
        return new String(buf) + text;
    }

    public String value() {
        return number;
    }

    @Override public String toString() {
        return number;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderNumber that = (OrderNumber) o;

        if (number != null ? !number.equals(that.number) : that.number != null) return false;

        return true;
    }

    @Override public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }
}