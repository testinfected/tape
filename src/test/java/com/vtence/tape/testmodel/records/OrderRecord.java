package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Table;
import com.vtence.tape.testmodel.Order;
import com.vtence.tape.testmodel.OrderNumber;
import com.vtence.tape.testmodel.PaymentMethod;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static com.vtence.tape.testmodel.Access.idOf;

public class OrderRecord extends AbstractRecord<Order> {

    private final Table<? extends PaymentMethod> payments;

    private final Table<Order> orders = new Table<Order>("orders", this);

    private final Column<Long> id = orders.LONG("id");
    private final Column<String> number = orders.STRING("number");
    private final Column<Long> payment = orders.LONG("payment_id");

    public static Table<Order> ordersWith(Table<? extends PaymentMethod> payments) {
        return new OrderRecord(payments).orders;
    }

    public OrderRecord(Table<? extends PaymentMethod> payments) {
        this.payments = payments;
    }

    public Order hydrate(ResultSet rs) throws SQLException {
        Order order = new Order(new OrderNumber(number.get(rs)));
        if (payment.get(rs) != Types.NULL)
            order.paidUsing(payments.hydrate(rs));
        idOf(order).set(id.get(rs));
        return order;
    }

    public void dehydrate(PreparedStatement st, Order order) throws SQLException {
        id.set(st, idOf(order).get());
        number.set(st, order.getNumber());
        payment.set(st, order.isPaid() ? idOf(order.getPaymentMethod()).get() : null);
    }
}