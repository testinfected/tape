package com.vtence.tape.testmodel.records;

import com.vtence.tape.Table;
import com.vtence.tape.TableSchema;
import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.LineItem;
import com.vtence.tape.testmodel.Order;
import com.vtence.tape.testmodel.PaymentMethod;
import com.vtence.tape.testmodel.Product;

import java.util.TimeZone;

public class Schema {

    private Schema() {}

    public static Table<Product> products() {
        TableSchema schema = new TableSchema("products");
        return new Table<>(schema, new ProductRecord(
                schema.LONG("id").autoGenerated(),
                schema.STRING("number"),
                schema.STRING("name"),
                schema.STRING("description")));
    }

    public static Table<Item> itemsOf(Table<Product> products) {
        TableSchema schema = new TableSchema("items");
        return new Table<>(schema, new ItemRecord(
                schema.LONG("id").autoGenerated(),
                schema.STRING("number"),
                schema.LONG("product_id"),
                schema.BIG_DECIMAL("price"),
                products));
    }

    public static Table<PaymentMethod> payments() {
        TableSchema schema = new TableSchema("payments");
        return new Table<>(schema, new PaymentRecord(
                schema.LONG("id").autoGenerated(),
                schema.STRING("payment_type"),
                schema.STRING("card_type"),
                schema.STRING("card_number"),
                schema.STRING("card_expiry_date"),
                schema.INT("card_verification_code")));
    }

    public static Table<Order> ordersWith(Table<PaymentMethod> payments) {
        TableSchema schema = new TableSchema("orders");
        return new Table<>(schema, new OrderRecord(
                schema.LONG("id").autoGenerated(),
                schema.STRING("number"),
                schema.LONG("payment_id"),
                schema.DATE("shipping_date", TimeZone.getTimeZone("UTC")),
                schema.TIME("shipping_time", TimeZone.getTimeZone("UTC")),
                schema.TIMESTAMP("ordered_at", TimeZone.getTimeZone("UTC")),
                payments));
    }

    public static Table<LineItem> lineItems() {
        TableSchema schema = new TableSchema("line_items");
        return new Table<>(schema, new LineItemRecord(
                schema.LONG("id").autoGenerated(),
                schema.STRING("item_number"),
                schema.BIG_DECIMAL("item_unit_price"),
                schema.LONG("order_id"),
                schema.INT("order_line")));
    }
}
