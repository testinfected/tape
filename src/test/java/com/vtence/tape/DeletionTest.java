package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.builders.Builder;
import com.vtence.tape.testmodel.records.Schema;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static com.vtence.tape.testmodel.matchers.Products.productNamed;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DeletionTest {

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
    deletingAllRecords() {
        persist(aProduct().named("English Bulldog"),
                aProduct().named("Labrador Retriever"),
                aProduct().named("Dalmatian"));

        transactor.perform(() -> {
            int deleted = Delete.from(products)
                                .execute(connection);
            assertThat("records deleted", deleted, is(3));
        });

        List<Product> records = Select.from(products)
                                      .list(connection);
        assertThat("records left", records, empty());
    }

    @SuppressWarnings("unchecked") @Test public void
    deletingAnExistingRecord() {
        persist(aProduct().withNumber("12345678").named("English Bulldog"),
                aProduct().named("Labrador Retriever"),
                aProduct().named("Dalmatian"));

        transactor.perform(() -> {
            int deleted = Delete.from(products)
                                .where("number = ?", "12345678")
                                .execute(executor);
            assertThat("records deleted", deleted, is(1));
        });

        List<Product> records = Select.from(products)
                                      .list(connection);
        assertThat("records left", records, containsInAnyOrder(productNamed("Labrador Retriever"), productNamed("Dalmatian")));
    }

    private void persist(final Builder<Product>... products) {
        for (final Builder<Product> product : products) {
            persist(product);
        }
    }

    private Product persist(final Builder<Product> builder) {
        return persist(builder.build());
    }

    private Product persist(final Product product) {
        transactor.perform(() -> Insert.into(products, product).execute(connection));

        return product;
    }
}