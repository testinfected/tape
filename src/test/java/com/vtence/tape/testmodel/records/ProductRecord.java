package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Record;
import com.vtence.tape.testmodel.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vtence.tape.testmodel.Access.idOf;

/**
 * An example of returning a new instance from {@link Record#handleKeys}
 */
public class ProductRecord implements Record<Product> {

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
        number.set(st, product.getNumber());
        name.set(st, product.getName());
        description.set(st, product.getDescription());
    }

    @Override
    public Product handleKeys(ResultSet keys, Product entity) throws SQLException {
        Product product = new Product(entity.getNumber(), entity.getName(), entity.getDescription());
        idOf(product).set(keys.getLong(1));
        return product;
    }
}
