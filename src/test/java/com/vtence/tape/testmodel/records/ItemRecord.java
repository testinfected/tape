package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Record;
import com.vtence.tape.testmodel.Access;
import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.Product;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRecord extends AbstractRecord<Item> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<Long> product;
    private final Column<BigDecimal> price;
    private final Column<Boolean> inStock;

    private final Record<Product> products;

    public ItemRecord(Column<Long> id,
                      Column<String> number,
                      Column<Long> product,
                      Column<BigDecimal> price,
                      Column<Boolean> inStock,
                      Record<Product> products) {
        this.id = id;
        this.number = number;
        this.product = product;
        this.price = price;
        this.inStock = inStock;
        this.products = products;
    }

    public Item hydrate(ResultSet rs) throws SQLException {
        Item item = new Item(number.get(rs), products.hydrate(rs), price.get(rs));
        item.setInStock(inStock.get(rs));
        Access.idOf(item).set(id.get(rs));
        return item;
    }

    public void dehydrate(PreparedStatement st, Item item) throws SQLException {
        number.set(st, item.getNumber());
        product.set(st, Access.idOf(item.getProduct()).get());
        price.set(st, item.getPrice());
        inStock.set(st, item.isInStock());
    }
}
