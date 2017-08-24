package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.support.UnitOfWork;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.builders.Builder;
import com.vtence.tape.testmodel.records.Schema;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CountTest {

    Database database = Database.in(memory());

    Connection connection = database.start();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = Schema.products();

    @After
    public void
    stopDatabase() {
        database.stop();
    }

    @Test
    public void
    countingTheNumberOfRecordsInATable() throws SQLException {
        assertEmpty(products);

        persist(
                aProduct().withNumber("00000001"),
                aProduct().withNumber("00000002"),
                aProduct().withNumber("00000003"),
                aProduct().withNumber("00000004"),
                aProduct().withNumber("00000005")
        );

        int count = Count.from(products).execute(connection);
        assertThat("records count", count, is(5));
    }

    private void assertEmpty(Table<?> table) throws SQLException {
        int count = Count.from(table).execute(connection);
        assertThat("records count", count, is(0));
    }

    @SafeVarargs
    private final <T> void persist(final Builder<T>... builders) {
        for (final Builder<T> builder : builders) {
            persist(builder);
        }
    }

    private <T> T persist(final Builder<T> builder) {
        return persist(builder.build());
    }

    private <T> T persist(final T entity) {
        transactor.perform(new UnitOfWork() {
            public void execute() {
                Insert.into(tableFor(entity), entity).execute(connection);
            }
        });
        return entity;
    }

    @SuppressWarnings("unchecked")
    private <T> Table<T> tableFor(T entity) {
        Map<Class, Table> tables = new HashMap<>();
        tables.put(Product.class, products);
        return (Table<T>) tables.get(entity.getClass());
    }
}
