package com.vtence.tape.testmodel.matchers;

import com.vtence.tape.testmodel.Order;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class Orders {

    public static Matcher<Order> orderWithNumber(Matcher<? super String> orderNumber) {
        return new FeatureMatcher<Order, String>(orderNumber, "an order with number", "order number") {
            @Override protected String featureValueOf(Order order) {
                return order.getNumber();
            }
        };
    }

    private Orders() {}
}
