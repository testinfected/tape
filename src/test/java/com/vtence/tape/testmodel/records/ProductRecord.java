package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Table;
import com.vtence.tape.testmodel.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vtence.tape.testmodel.Access.idOf;

public class ProductRecord extends AbstractRecord<Product> {

    private final Table<Product> products = new Table<Product>("products", this);

    private final Column<Long> id = products.LONG("id");
    private final Column<String> number = products.STRING("number");
    private final Column<String> name = products.STRING("name");
    private final Column<String> description = products.STRING("description");

    public static Table<Product> products() {
        return new ProductRecord().products;
    }

    public Product hydrate(ResultSet rs) throws SQLException {
        Product product = new Product(number.get(rs), name.get(rs), description.get(rs));
        idOf(product).set(id.get(rs));
        return product;
    }

    public void dehydrate(PreparedStatement st, Product product) throws SQLException {
        id.set(st, idOf(product).get());
        number.set(st, product.getNumber());
        name.set(st, product.getName());
        description.set(st, product.getDescription());
    }

    public void handleKeys(ResultSet keys, Product product) throws SQLException {
        idOf(product).set(generatedId(keys));
    }

    private long generatedId(ResultSet rs) throws SQLException {
        rs.first();
        return rs.getLong(1);
    }
}