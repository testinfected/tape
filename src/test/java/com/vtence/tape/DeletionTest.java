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
import java.util.List;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static com.vtence.tape.testmodel.matchers.Products.productNamed;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DeletionTest {

    Database database = Database.in(memory());
    Connection connection = database.start();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = Schema.products();

    @After public void
    stopDatabase() {
        database.stop();
    }

    @SuppressWarnings("unchecked") @Test public void
    deletingAllRecords() {
        persist(aProduct().named("English Bulldog"),
                aProduct().named("Labrador Retriever"),
                aProduct().named("Dalmatian"));

        transactor.perform(new UnitOfWork() {
            public void execute() {
                int deleted = Delete.from(products).execute(connection);
                assertThat("records deleted", deleted, is(3));
            }
        });

        List<Product> records = Select.from(products).list(connection);
        assertThat("existing records", records, empty());
    }

    @SuppressWarnings("unchecked") @Test public void
    deletingAnExistingRecord() {
        persist(aProduct().withNumber("12345678").named("English Bulldog"),
                aProduct().named("Labrador Retriever"),
                aProduct().named("Dalmatian"));

        transactor.perform(new UnitOfWork() {
            public void execute() {
                int deleted = Delete.from(products).where("number = ?", "12345678").execute(connection);
                assertThat("records deleted", deleted, is(1));
            }
        });

        List<Product> records = Select.from(products).list(connection);
        assertThat("existing records", records, hasSize(2));
        assertThat("existing records", records, containsInAnyOrder(productNamed("Labrador Retriever"), productNamed("Dalmatian")));
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
        transactor.perform(new UnitOfWork() {
            public void execute() {
                Insert.into(products, product).execute(connection);
            }
        });

        return product;
    }
}