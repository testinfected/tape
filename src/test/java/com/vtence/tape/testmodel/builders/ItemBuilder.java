package com.vtence.tape.testmodel.builders;

import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.Product;

import java.math.BigDecimal;

import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;

public class ItemBuilder implements Builder<Item> {

    private String number = FakeNumber.aNumber();
    private Product product = aProduct().build();
    private BigDecimal price = FakePrice.aPrice();
    private boolean inStock;

    public static ItemBuilder anItem() {
        return new ItemBuilder();
    }

    public Item build() {
        Item item = new Item(number, product, price);
        item.setInStock(inStock);
        return item;
    }

    public ItemBuilder withNumber(String number) {
        this.number = number;
        return this;
    }

    public static ItemBuilder a(ProductBuilder productBuilder) {
        return a(productBuilder.build());
    }

    public static ItemBuilder an(ProductBuilder productBuilder) {
        return an(productBuilder.build());
    }

    public static ItemBuilder a(Product product) {
        return anItem().of(product);
    }

    public static ItemBuilder an(Product product) {
        return a(product);
    }

    public ItemBuilder of(ProductBuilder product) {
        return of(product.build());
    }

    public ItemBuilder of(Product product) {
        this.product = product;
        return this;
    }

    public ItemBuilder priced(String price) {
        return priced(new BigDecimal(price));
    }

    public ItemBuilder inStock() {
        inStock = true;
        return this;
    }

    public ItemBuilder priced(BigDecimal price) {
        this.price = price;
        return this;
    }
}
