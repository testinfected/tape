package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.support.UnitOfWork;
import com.vtence.tape.testmodel.Access;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.records.Schema;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class InsertionTest {

    Database database = Database.in(memory());
    Connection connection = database.start();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = Schema.products();

    @After public void
    stopDatabase() {
        database.stop();
    }

    @Test public void
    insertingANewRecord() {
        final Product original = aProduct().withNumber("12345678").named("English Bulldog").describedAs("A muscular heavy dog").build();

        transactor.perform(new UnitOfWork() {
            public void execute() {
                int inserted = Insert.into(products, original).execute(connection);
                assertThat("records inserted", inserted, is(1));
            }
        });

        Product record = Select.from(products).first(connection);
        assertThat("inserted record", record, samePropertyValuesAs(original));
    }

    @Test public void
    insertingAnEntityWillSetItsIdentifier() {
        final Product entity = aProduct().build();
        assertThat("orginal id", idOf(entity), nullValue());

        transactor.perform(new UnitOfWork() {
            public void execute() {
                Insert.into(products, entity).execute(connection);
            }
        });

        assertThat("updated id", idOf(entity), not(nullValue()));
    }

    private Long idOf(Object entity) {
        return Access.idOf(entity).get();
    }
}
