package com.vtence.tape.testmodel.matchers;

import com.vtence.tape.testmodel.Item;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

public class Items {

    public static Matcher<Item> itemWithProductNumber(final String number) {
        return new FeatureMatcher<Item, String>(equalTo(number), "has product number", "product number") {
            @Override protected String featureValueOf(Item actual) {
                return actual.getProductNumber();
            }
        };
    }

    public Items() {}
}
