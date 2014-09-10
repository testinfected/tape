package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.testmodel.LineItem;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vtence.tape.testmodel.Access.idOf;
import static com.vtence.tape.testmodel.Access.orderOf;

public class LineItemRecord extends AbstractRecord<LineItem> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<BigDecimal> unitPrice;
    private final Column<Long> order;
    private final Column<Integer> line;

    public LineItemRecord(Column<Long> id,
                          Column<String> number,
                          Column<BigDecimal> unitPrice,
                          Column<Long> order,
                          Column<Integer> line) {
        this.id = id;
        this.number = number;
        this.unitPrice = unitPrice;
        this.order = order;
        this.line = line;
    }

    public LineItem hydrate(ResultSet rs) throws SQLException {
        LineItem lineItem = new LineItem(number.get(rs), unitPrice.get(rs));
        idOf(lineItem).set(id.get(rs));
        return lineItem;
    }

    public void dehydrate(PreparedStatement st, LineItem lineItem) throws SQLException {
        id.set(st, idOf(lineItem).get());
        number.set(st, lineItem.getItemNumber());
        unitPrice.set(st, lineItem.getItemUnitPrice());
        order.set(st, idOf(orderOf(lineItem).get()).get());
        line.set(st, orderOf(lineItem).get().indexOf(lineItem));
    }
}