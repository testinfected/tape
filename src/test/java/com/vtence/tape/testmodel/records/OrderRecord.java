package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Record;
import com.vtence.tape.testmodel.Order;
import com.vtence.tape.testmodel.OrderNumber;
import com.vtence.tape.testmodel.PaymentMethod;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;

import static com.vtence.tape.JDBC.toJavaDate;
import static com.vtence.tape.JDBC.toSQLTime;
import static com.vtence.tape.testmodel.Access.idOf;

public class OrderRecord extends AbstractRecord<Order> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<Long> payment;
    private final Column<Time> shippingTime;

    private final Record<? extends PaymentMethod> payments;

    public OrderRecord(Column<Long> id, Column<String> number, Column<Long> payment,
                       Column<Time> shippingTime, Record<? extends PaymentMethod> payments) {
        this.shippingTime = shippingTime;
        this.payments = payments;
        this.id = id;
        this.number = number;
        this.payment = payment;
    }

    public Order hydrate(ResultSet rs) throws SQLException {
        Order order = new Order(new OrderNumber(number.get(rs)));
        if (payment.get(rs) != Types.NULL)
            order.paidUsing(payments.hydrate(rs));
        order.setShippingTime(toJavaDate(shippingTime.get(rs)));
        idOf(order).set(id.get(rs));
        return order;
    }

    public void dehydrate(PreparedStatement st, Order order) throws SQLException {
        number.set(st, order.getNumber());
        payment.set(st, order.isPaid() ? idOf(order.getPaymentMethod()).get() : null);
        shippingTime.set(st, toSQLTime(order.getShippingTime()));
    }
}
