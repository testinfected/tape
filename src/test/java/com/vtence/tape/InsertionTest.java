package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.support.TestEnvironment;
import com.vtence.tape.support.UnitOfWork;
import com.vtence.tape.testmodel.Access;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.records.ProductRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

public class InsertionTest {

    Database database = Database.in(TestEnvironment.inMemory());
    Connection connection = database.connect();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = ProductRecord.products();

    @Before public void
    resetDatabase() throws Exception {
        database.reset();
    }

    @After public void
    closeConnection() {
        JDBC.close(connection);
    }

    @Test public void
    insertingANewRecord() throws Exception {
        Product original = aProduct().withNumber("12345678").named("English Bulldog").describedAs("A muscular heavy dog").build();
        final Insert<Product> insert = Insert.into(products, original);

        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                insert.execute(connection);
            }
        });

        Product record = Select.from(products).single(connection);
        assertThat("inserted record", record, samePropertyValuesAs(original));
    }

    @Test public void
    insertingAEntityWillSetItsIdentifier() throws Exception {
        Product product = aProduct().build();
        assertThat("orginal id", idOf(product), nullValue());

        final Insert<Product> insert = Insert.into(products, product);
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                insert.execute(connection);
            }
        });

        assertThat("updated id", idOf(product), not(nullValue()));
    }

    private Long idOf(Object entity) {
        return Access.idOf(entity).get();
    }
}
