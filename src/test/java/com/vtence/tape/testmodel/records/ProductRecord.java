package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.testmodel.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vtence.tape.testmodel.Access.idOf;

public class ProductRecord extends AbstractRecord<Product> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<String> name;
    private final Column<String> description;

    public ProductRecord(Column<Long> id,
                         Column<String> number,
                         Column<String> name,
                         Column<String> description) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.description = description;
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
}