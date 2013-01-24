package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.support.TestEnvironment;
import com.vtence.tape.support.UnitOfWork;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.builders.Builder;
import com.vtence.tape.testmodel.records.ProductRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static com.vtence.tape.testmodel.Access.idOf;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

public class UpdateTest {

    Database database = Database.in(TestEnvironment.inMemory());
    Connection connection = database.connect();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = ProductRecord.products();

    @Before public void
    resetDatabase() throws Exception {
        database.reset();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @Test public void
    updatingAnExistingRecord() throws Exception {
        Product product = persist(aProduct().withNumber("12345678").named("English Bulldog").describedAs("A muscular heavy dog"));

        product.setName("Labrador Retriever");
        product.setDescription("A fun type of dog");
        final Update update = Update.set(products, product).where("id = ?", idOf(product));
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                update.execute(connection);
            }
        });

        Product record = Select.from(products).single(connection);
        assertThat("updated record", record, samePropertyValuesAs(product));
    }

    private Product persist(Builder<Product> productBuilder) throws Exception {
        return persist(productBuilder.build());
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
