package com.vtence.tape.testmodel;

import java.math.BigDecimal;

public class Item extends Entity {

    private final String number;
    private final Product product;
    private final BigDecimal price;
    private boolean inStock;

    public Item(String number, Product product, BigDecimal price) {
        this.number = number;
        this.product = product;
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductNumber() {
        return product.getNumber();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public boolean isInStock() {
        return inStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (number != null ? !number.equals(item.number) : item.number != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    public String toString() {
        return number + " (" + product.getNumber() + ")";
    }
}