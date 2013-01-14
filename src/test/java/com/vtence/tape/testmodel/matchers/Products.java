package com.vtence.tape.testmodel.matchers;

import com.vtence.tape.testmodel.Product;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

public class Products {

    public static Matcher<? super Product> productNamed(String name) {
        return new FeatureMatcher<Product, String>(equalTo(name), "a product with name", "name") {
            @Override protected String featureValueOf(Product actual) {
                return actual.getName();
            }
        };
    }

    public static Matcher<? super Product> productWithNumber(String number) {
        return new FeatureMatcher<Product, String>(equalTo(number), "a product with number", "number") {
            @Override protected String featureValueOf(Product actual) {
                return actual.getNumber();
            }
        };
    }

    private Products() {}
}
