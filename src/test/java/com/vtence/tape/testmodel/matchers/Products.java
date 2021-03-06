package com.vtence.tape.testmodel.matchers;

import com.vtence.tape.testmodel.Product;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class Products {

    public static Matcher<Product> sameProductAs(Product original) {
        return samePropertyValuesAs(original);
    }

    public static Matcher<? super Product> productNamed(String name) {
        return new FeatureMatcher<Product, String>(equalTo(name), "a product with name", "name") {
            protected String featureValueOf(Product actual) {
                return actual.getName();
            }
        };
    }

    public static Matcher<? super Product> productWithNumber(String number) {
        return new FeatureMatcher<Product, String>(equalTo(number), "a product with number", "number") {
            protected String featureValueOf(Product actual) {
                return actual.getNumber();
            }
        };
    }

    private Products() {}
}
