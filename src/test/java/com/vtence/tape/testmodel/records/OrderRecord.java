package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Record;
import com.vtence.tape.testmodel.Order;
import com.vtence.tape.testmodel.OrderNumber;
import com.vtence.tape.testmodel.PaymentMethod;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import static com.vtence.tape.JDBC.toJavaDate;
import static com.vtence.tape.JDBC.toSQLDate;
import static com.vtence.tape.JDBC.toSQLTime;
import static com.vtence.tape.JDBC.toSQLTimestamp;
import static com.vtence.tape.testmodel.Access.idOf;

public class OrderRecord extends AbstractRecord<Order> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<Long> payment;
    private final Column<Date> shippingDate;
    private final Column<Time> shippingTime;
    private final Column<Timestamp> placedAt;

    private final Record<? extends PaymentMethod> payments;

    public OrderRecord(Column<Long> id,
                       Column<String> number,
                       Column<Long> payment,
                       Column<Date> shippingDate,
                       Column<Time> shippingTime,
                       Column<Timestamp> placedAt,
                       Record<? extends PaymentMethod> payments) {
        this.id = id;
        this.number = number;
        this.payment = payment;
        this.placedAt = placedAt;
        this.shippingDate = shippingDate;
        this.shippingTime = shippingTime;
        this.payments = payments;
    }

    public Order hydrate(ResultSet rs) throws SQLException {
        Order order = new Order(new OrderNumber(number.get(rs)));
        if (payment.get(rs) != Types.NULL)
            order.paidUsing(payments.hydrate(rs));
        order.setShippingDate(toJavaDate(shippingDate.get(rs)));
        order.setShippingTime(toJavaDate(shippingTime.get(rs)));
        order.setPlacedAt(toJavaDate(placedAt.get(rs)));
        idOf(order).set(id.get(rs));
        return order;
    }

    public void dehydrate(PreparedStatement st, Order order) throws SQLException {
        number.set(st, order.getNumber());
        payment.set(st, order.isPaid() ? idOf(order.getPaymentMethod()).get() : null);
        shippingDate.set(st, toSQLDate(order.getShippingDate()));
        shippingTime.set(st, toSQLTime(order.getShippingTime()));
        placedAt.set(st, toSQLTimestamp(order.getPlacedAt()));
    }
}
