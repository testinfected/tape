package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Table;
import com.vtence.tape.testmodel.LineItem;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vtence.tape.testmodel.Access.idOf;
import static com.vtence.tape.testmodel.Access.orderOf;

public class LineItemRecord extends AbstractRecord<LineItem> {

    private final Table<LineItem> lineItems = new Table<LineItem>("line_items", this);

    private final Column<Long> id = lineItems.LONG("id");
    private final Column<String> number = lineItems.STRING("item_number");
    private final Column<BigDecimal> unitPrice = lineItems.BIG_DECIMAL("item_unit_price");
    private final Column<Long> order = lineItems.LONG("order_id");
    private final Column<Integer> line = lineItems.INT("order_line");

    public static Table<LineItem> lineItems() {
        return new LineItemRecord().lineItems;
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
