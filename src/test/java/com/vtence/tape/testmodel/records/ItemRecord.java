package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Table;
import com.vtence.tape.testmodel.Access;
import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.Product;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRecord extends AbstractRecord<Item> {

    private final Table<Product> products;

    private final Table<Item> items = new Table<Item>("items", this);

    private final Column<Long> id = items.LONG("id");
    private final Column<String> number = items.STRING("number");
    private final Column<Long> product = items.LONG("product_id");
    private final Column<BigDecimal> price = items.BIG_DECIMAL("price");

    public static Table<Item> itemsOf(Table<Product> products) {
        return new ItemRecord(products).items;
    }

    public ItemRecord(Table<Product> products) {
        this.products = products;
    }

    public Item hydrate(ResultSet rs) throws SQLException {
        Item item = new Item(number.get(rs), products.hydrate(rs), price.get(rs));
        Access.idOf(item).set(id.get(rs));
        return item;
    }

    public void dehydrate(PreparedStatement st, Item item) throws SQLException {
        id.set(st, Access.idOf(item).get());
        number.set(st, item.getNumber());
        product.set(st, Access.idOf(Access.productOf(item).get()).get());
        price.set(st, item.getPrice());
    }
}