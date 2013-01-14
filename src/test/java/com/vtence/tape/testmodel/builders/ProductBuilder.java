package com.vtence.tape.testmodel.builders;

import com.vtence.tape.testmodel.Product;

public class ProductBuilder implements Builder<Product> {

    private String number = FakeNumber.aNumber();
    private String name = "a product";
    private String description = "";

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public static ProductBuilder aProduct(String productNumber) {
        return aProduct().withNumber(productNumber);
    }

    public ProductBuilder withNumber(String number) {
        this.number = number;
        return this;
    }

    public ProductBuilder named(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder describedAs(String description) {
        this.description = description;
        return this;
    }

    public Product build() {
        return new Product(number, name, description);
    }
}
