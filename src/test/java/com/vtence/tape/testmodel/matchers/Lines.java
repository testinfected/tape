package com.vtence.tape.testmodel.matchers;

import com.vtence.tape.testmodel.LineItem;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

public class Lines {

    public static Matcher<LineItem> lineWithItemNumber(String itemNumber) {
        return new FeatureMatcher<LineItem, String>(equalTo(itemNumber), "an order line for item", "item") {
            protected String featureValueOf(LineItem line) {
                return line.getItemNumber();
            }
        };
    }

    private Lines() {}
}
