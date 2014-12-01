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

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

public class UpdateTest {

    Database database = Database.in(memory());
    Connection connection = database.start();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = Schema.products();

    @After public void
    stopDatabase() {
        database.stop();
    }

    @Test public void
    updatingAnExistingRecord() {
        persist(aProduct().named("Dalmatian"));
        final Product original = persist(aProduct().withNumber("12345678").named("English Bulldog").describedAs("A muscular heavy dog"));

        original.setName("Labrador Retriever");
        original.setDescription("A fun type of dog");
        transactor.perform(new UnitOfWork() {
            public void execute() {
                int updated = Update.set(products, original).where("number = ?", "12345678").execute(connection);
                assertThat("records updated", updated, is(1));
            }
        });

        Product record = Select.from(products).where("name = ?", "Labrador Retriever").first(connection);
        assertThat("updated record", record, samePropertyValuesAs(original));
    }

    private Product persist(Builder<Product> productBuilder) {
        return persist(productBuilder.build());
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
