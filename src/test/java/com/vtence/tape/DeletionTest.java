package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.support.TestEnvironment;
import com.vtence.tape.support.UnitOfWork;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.builders.Builder;
import com.vtence.tape.testmodel.records.Schema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static com.vtence.tape.testmodel.matchers.Products.productNamed;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DeletionTest {

    Database database = Database.in(TestEnvironment.inMemory());
    Connection connection = database.connect();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = Schema.products();

    @Before public void
    resetDatabase() throws Exception {
        database.reset();
    }

    @After public void
    closeConnection() {
        JDBC.close(connection);
    }

    @SuppressWarnings("unchecked") @Test public void
    deletingAllRecords() throws Exception {
        persist(aProduct().named("English Bulldog"),
                aProduct().named("Labrador Retriever"),
                aProduct().named("Dalmatian"));

        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                int deleted = Delete.from(products).execute(connection);
                assertThat("records deleted", deleted, is(3));
            }
        });

        List<Product> records = Select.from(products).list(connection);
        assertThat("existing records", records, empty());
    }

    @SuppressWarnings("unchecked") @Test public void
    deletingAnExistingRecord() throws Exception {
        persist(aProduct().withNumber("12345678").named("English Bulldog"),
                aProduct().named("Labrador Retriever"),
                aProduct().named("Dalmatian"));

        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                int deleted = Delete.from(products).where("number = ?", "12345678").execute(connection);
                assertThat("records deleted", deleted, is(1));
            }
        });

        List<Product> records = Select.from(products).list(connection);
        assertThat("existing records", records, hasSize(2));
        assertThat("existing records", records, containsInAnyOrder(productNamed("Labrador Retriever"), productNamed("Dalmatian")));
    }

    private void persist(final Builder<Product>... products) throws Exception {
        for (final Builder<Product> product : products) {
            persist(product);
        }
    }

    private Product persist(final Builder<Product> builder) throws Exception {
        return persist(builder.build());
    }

    private Product persist(final Product product) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                Insert.into(products, product).execute(connection);
            }
        });

        return product;
    }
}