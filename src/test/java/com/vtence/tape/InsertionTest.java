package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.testmodel.Access;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.records.Schema;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.util.Optional;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class InsertionTest {

    Database database = Database.in(memory());
    Connection connection = database.start();
    /**
     * An alternative to using a <code>Connection</code> is to provide a {@link StatementExecutor}
     */
    StatementExecutor executor = database::execute;

    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = Schema.products();

    @After public void
    stopDatabase() {
        database.stop();
    }

    @Test public void
    insertingANewRecord() {
        final Product original = aProduct().withNumber("12345678").named("English Bulldog").describedAs("A muscular heavy dog").build();

        transactor.perform(() -> {
            Product inserted = Insert.into(products, original)
                                     .execute(connection);
            assertThat("record inserted", inserted, not(nullValue()));
            assertThat("generated id", idOf(inserted), not(nullValue()));
        });

        Product record = assertFound(Select.from(products)
                                           .first(connection));
        assertThat("inserted record", record, samePropertyValuesAs(original));
    }

    private Long idOf(Object entity) {
        return Access.idOf(entity).get();
    }

    private <T> T assertFound(Optional<T> record) {
        assertThat("found", record.isPresent(), CoreMatchers.is(true));
        return record.get();
    }
}
