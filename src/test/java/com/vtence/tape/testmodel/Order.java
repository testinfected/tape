package com.vtence.tape.testmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Order extends Entity {

    private final OrderNumber number;
    private final List<LineItem> lines = new ArrayList<>();

    private PaymentMethod paymentMethod;
    private Date shippingDate;
    private Date shippingTime;

    public Order(OrderNumber number) {
        this.number = number;
    }

    public String getNumber() {
        return number.value();
    }

    public void addItem(Item item) {
        addLineItem(LineItem.from(item));
    }

    public void addLineItem(LineItem lineItem) {
        lines.add(lineItem);
    }

    public List<LineItem> getLineItems() {
        return Collections.unmodifiableList(lines);
    }

    public void paidUsing(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isPaid() {
        return paymentMethod != null;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date date) {
        this.shippingDate = date;
    }

    public Date getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(Date time) {
        this.shippingTime = time;
    }

    public int getLineItemCount() {
        return lines.size();
    }

    public int indexOf(LineItem lineItem) {
        return lines.indexOf(lineItem);
    }

    public String toString() {
        return "#" + number.toString();
    }
}
