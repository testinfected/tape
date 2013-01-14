package com.vtence.tape.testmodel;

import java.math.BigDecimal;

public class LineItem extends Entity {

    @SuppressWarnings("unused")
    private Order order;
    private final String itemNumber;
    private final BigDecimal itemUnitPrice;
    private int quantity;

    public static LineItem from(Item item) {
        return new LineItem(
                item.getNumber(),
                item.getPrice());
    }

    public LineItem(String itemNumber, BigDecimal itemUnitPrice) {
        this.itemNumber = itemNumber;
        this.itemUnitPrice = itemUnitPrice;
        this.quantity = 1;
    }

    public void incrementQuantity(int count) {
        quantity += count;
    }

    public BigDecimal getItemUnitPrice() {
        return itemUnitPrice;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public int quantity() {
        return quantity;
    }

    public String toString() {
        return itemNumber + " (" + itemUnitPrice + "$ x " + quantity + ")";
    }
}
