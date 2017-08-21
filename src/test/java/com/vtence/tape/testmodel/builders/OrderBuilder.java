package com.vtence.tape.testmodel.builders;

import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.Order;
import com.vtence.tape.testmodel.OrderNumber;
import com.vtence.tape.testmodel.PaymentMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class OrderBuilder implements Builder<Order> {

    private OrderNumber orderNumber = new OrderNumber(FakeNumber.aNumber());
    private final List<Item> items = new ArrayList<Item>();
    private PaymentMethod paymentMethod;
    private Date shippingDate;
    private Date shippingTime;
    private Date orderedAt;

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public OrderBuilder withNumber(String orderNumber) {
        return with(new OrderNumber(orderNumber));
    }

    public OrderBuilder with(final OrderNumber orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public OrderBuilder containing(ItemBuilder... items) {
        for (ItemBuilder item : items) {
            containing(item.build());
        }
        return this;
    }

    public OrderBuilder containing(Item... items) {
        this.items.addAll(asList(items));
        return this;
    }

    public OrderBuilder paidUsing(Builder<? extends PaymentMethod> paymentMethodBuilder) {
        return paidUsing(paymentMethodBuilder.build());
    }

    public OrderBuilder paidUsing(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderBuilder shippedOn(Date date) {
        shippingDate = date;
        return this;
    }

    public OrderBuilder shippedAt(Date time) {
        this.shippingTime = time;
        return this;
    }

    public OrderBuilder orderedAt(Date instant) {
        this.orderedAt = instant;
        return this;
    }

    public Order build() {
        Order order = new Order(orderNumber);
        for (Item item : items) {
            order.addItem(item);
        }
        order.paidUsing(paymentMethod);
        order.setShippingDate(shippingDate);
        order.setShippingTime(shippingTime);
        order.setOrderedAt(orderedAt);
        return order;
    }
}